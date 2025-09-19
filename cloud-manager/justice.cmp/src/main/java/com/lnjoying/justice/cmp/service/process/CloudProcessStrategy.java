package com.lnjoying.justice.cmp.service.process;

import com.lnjoying.justice.cmp.common.DataSyncLevel;
import com.lnjoying.justice.cmp.common.ProcessorName;
import com.lnjoying.justice.cmp.domain.model.CreateResourceInfo;
import com.lnjoying.justice.cmp.domain.model.SyncResourceInfo;
import com.lnjoying.justice.cmp.service.process.processor.CreateResourceProcessor;
import com.lnjoying.justice.cmp.service.process.processor.HealthCheckProcessor;
import com.lnjoying.justice.cmp.service.process.processor.SyncSingleDataProcessor;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.common.Pair;
import com.micro.core.process.service.ProcessMultiStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.PriorityBlockingQueue;

@Service("cloudProcessStrategy")
public class CloudProcessStrategy extends ProcessMultiStrategy
{
	public CloudProcessStrategy(String desc, int threadNum, int maxQueueSize)
	{
		super(desc, threadNum, maxQueueSize);
	}

	private static Logger LOGGER = LogManager.getLogger();

	private static PriorityBlockingQueue<SyncResourceInfo> hotResourceQueue = new PriorityBlockingQueue<>(11, new Comparator<SyncResourceInfo>() {
		@Override
		public int compare(SyncResourceInfo o1, SyncResourceInfo o2) {
			return Long.compare(o1.getNextSyncTime(), o2.getNextSyncTime());
		}
	});

	@Autowired
	private HealthCheckProcessor healthCheckProcessor;

	@Autowired
	private SyncSingleDataProcessor syncSingleDataProcessor;

	@Autowired
	private CreateResourceProcessor createResourceProcessor;

	public CloudProcessStrategy()
	{
		super("cmp message service",3, 10000);
		LOGGER.info("init cloud process strategy");
	}
	
	@PostConstruct
	public void start()
	{
		super.start(() -> healthCheckProcessor, 1, ProcessorName.HEALTH_CHECK);
		super.start(() -> syncSingleDataProcessor, 1, ProcessorName.SYNC_DATA);
		super.start(() -> createResourceProcessor, 1, ProcessorName.CREATE_RESOURCE);
	}

	public void syncSingleData(SyncResourceInfo syncResourceInfo)
	{
		syncSingleData(syncResourceInfo, true, 0);
	}

	public void syncSingleData(SyncResourceInfo syncResourceInfo, boolean ignoreLevel)
	{
		syncSingleData(syncResourceInfo, ignoreLevel, new Date().getTime());
	}

	public void syncSingleData(SyncResourceInfo syncResourceInfo, boolean ignoreLevel, long time)
	{
		MessagePack messagePack = new MessagePack();
		messagePack.setMessageObj(new Pair<>(syncResourceInfo.getCloudId(), syncResourceInfo.getResourceId()));
		messagePack.setMsgType(syncResourceInfo.getSyncMsg());
		sendMessageIfNotInQueue(messagePack, ProcessorName.SYNC_DATA);
		if (!ignoreLevel)
		{
			boolean reduceLevelResult = syncResourceInfo.reduceLevel(time);
			if (reduceLevelResult)
			{
				if (hotResourceQueue.contains(syncResourceInfo) && syncResourceInfo.getDataSyncLevel().getLevel() > DataSyncLevel.LEVEL_4.getLevel())
				{
					hotResourceQueue.remove(syncResourceInfo);
				}
				hotResourceQueue.offer(syncResourceInfo);
			}
		}
	}

	public void syncHotResource(Long time)
	{
		if (hotResourceQueue.isEmpty()) return;

		SyncResourceInfo syncResourceInfo = hotResourceQueue.peek();
		if (syncResourceInfo == null || syncResourceInfo.getNextSyncTime() > time)
		{
			return;
		}

		hotResourceQueue.poll();

		syncSingleData(syncResourceInfo, false, time);

		syncHotResource(time);
	}

	public void createResource(CreateResourceInfo createResourceInfo, String createResourceMsg)
	{
		MessagePack messagePack = new MessagePack();
		messagePack.setMessageObj(createResourceInfo);
		messagePack.setMsgType(createResourceMsg);
		sendMessageIfNotInQueue(messagePack, ProcessorName.CREATE_RESOURCE);
	}
}
