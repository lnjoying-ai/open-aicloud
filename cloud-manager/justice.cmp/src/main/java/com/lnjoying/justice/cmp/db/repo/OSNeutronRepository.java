package com.lnjoying.justice.cmp.db.repo;

import com.lnjoying.justice.cmp.db.mapper.*;
import com.lnjoying.justice.cmp.db.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class OSNeutronRepository
{
	@Autowired
	private TblCmpOsSubnetsMapper tblCmpOsSubnetsMapper;

	@Autowired
	private TblCmpOsIpallocationpoolsMapper tblCmpOsIpallocationpoolsMapper;

	@Autowired
	private TblCmpOsSubnetroutesMapper tblCmpOsSubnetroutesMapper;

	@Autowired
	private TblCmpOsDnsnameserversMapper tblCmpOsDnsnameserversMapper;

	@Autowired
	private TblCmpOsSubnetServiceTypesMapper tblCmpOsSubnetServiceTypesMapper;

	@Autowired
	private TblCmpOsNetworksMapper tblCmpOsNetworksMapper;

	@Autowired
	private TblCmpOsNetworksegmentsMapper tblCmpOsNetworksegmentsMapper;

	@Autowired
	private TblCmpOsPortsMapper tblCmpOsPortsMapper;

	@Autowired
	private TblCmpOsAllowedaddresspairsMapper tblCmpOsAllowedaddresspairsMapper;

	@Autowired
	private TblCmpOsMl2PortBindingsMapper tblCmpOsMl2PortBindingsMapper;

	@Autowired
	private TblCmpOsMl2PortBindingLevelsMapper tblCmpOsMl2PortBindingLevelsMapper;

	@Autowired
	private TblCmpOsMl2DistributedPortBindingsMapper tblCmpOsMl2DistributedPortBindingsMapper;

	@Autowired
	private TblCmpOsExtradhcpoptsMapper tblCmpOsExtradhcpoptsMapper;

	@Autowired
	private TblCmpOsSecuritygroupportbindingsMapper tblCmpOsSecuritygroupportbindingsMapper;

	@Autowired
	private TblCmpOsIpallocationsMapper tblCmpOsIpallocationsMapper;

	@Autowired
	private TblCmpOsFloatingipsMapper tblCmpOsFloatingipsMapper;

	@Autowired
	private TblCmpOsPortforwardingsMapper tblCmpOsPortforwardingsMapper;

	@Autowired
	private TblCmpOsRoutersMapper tblCmpOsRoutersMapper;

	@Autowired
	private TblCmpOsRouterroutesMapper tblCmpOsRouterroutesMapper;

	@Autowired
	private TblCmpOsRouterportsMapper tblCmpOsRouterportsMapper;

	@Autowired
	private TblCmpOsDvrHostMacsMapper tblCmpOsDvrHostMacsMapper;

	@Autowired
	private TblCmpOsSecuritygroupsMapper tblCmpOsSecuritygroupsMapper;

	@Autowired
	private TblCmpOsSecuritygrouprulesMapper tblCmpOsSecuritygrouprulesMapper;

	@Autowired
	private ManualMapper manualMapper;

	public TblCmpOsSubnets getSubnetById(String cloudId, String subnetId)
	{
		return tblCmpOsSubnetsMapper.selectByPrimaryKey(new TblCmpOsSubnetsKey(cloudId, subnetId));
	}

	public int insertSubnet(TblCmpOsSubnets tblCmpOsSubnets)
	{
		return tblCmpOsSubnetsMapper.insert(tblCmpOsSubnets);
	}

	public List<TblCmpOsSubnets> getSubnets(TblCmpOsSubnetsExample example)
	{
		return tblCmpOsSubnetsMapper.selectByExample(example);
	}

	public long countSubnetsByExample(TblCmpOsSubnetsExample example)
	{
		return tblCmpOsSubnetsMapper.countByExample(example);
	}

	public int updateSubnet(TblCmpOsSubnets tblCmpOsSubnets)
	{
		return tblCmpOsSubnetsMapper.updateByPrimaryKeySelective(tblCmpOsSubnets);
	}

	public int deleteSubnet(String cloudId, String subnetId)
	{
		TblCmpOsSubnets tblCmpOsSubnet = new TblCmpOsSubnets();
		tblCmpOsSubnet.setCloudId(cloudId);
		tblCmpOsSubnet.setId(subnetId);
		tblCmpOsSubnet.setEeStatus(REMOVED);

		return updateSubnet(tblCmpOsSubnet);
	}

	public Set<String> getSubnetIds(String cloudId)
	{
		return tblCmpOsSubnetsMapper.getSubnetIds(cloudId);
	}

	public List<TblCmpOsSubnets> getSubnetsWithShared(TblCmpOsSubnetsExample example, String cloudId, String projectId, String userId)
	{
		return tblCmpOsSubnetsMapper.selectByExampleWithShared(example, cloudId, projectId, userId);
	}

	public int insertIpallocationpool(TblCmpOsIpallocationpools tblCmpOsIpallocationpool)
	{
		return tblCmpOsIpallocationpoolsMapper.insert(tblCmpOsIpallocationpool);
	}

	public List<TblCmpOsIpallocationpools> getIpallocationpools(TblCmpOsIpallocationpoolsExample example)
	{
		return tblCmpOsIpallocationpoolsMapper.selectByExample(example);
	}

	public long countIpallocationpoolsByExample(TblCmpOsIpallocationpoolsExample example)
	{
		return tblCmpOsIpallocationpoolsMapper.countByExample(example);
	}

	public int updateIpallocationpool(TblCmpOsIpallocationpools tblCmpOsIpallocationpools)
	{
		return tblCmpOsIpallocationpoolsMapper.updateByPrimaryKeySelective(tblCmpOsIpallocationpools);
	}

	public int insertSubnetroute(TblCmpOsSubnetroutes tblCmpOsSubnetroutes)
	{
		return tblCmpOsSubnetroutesMapper.insert(tblCmpOsSubnetroutes);
	}

	public List<TblCmpOsSubnetroutes> getSubnetroutes(TblCmpOsSubnetroutesExample example)
	{
		return tblCmpOsSubnetroutesMapper.selectByExample(example);
	}

	public long countSubnetroutesByExample(TblCmpOsSubnetroutesExample example)
	{
		return tblCmpOsSubnetroutesMapper.countByExample(example);
	}

	public int updateSubnetroute(TblCmpOsSubnetroutes tblCmpOsSubnetroutes)
	{
		return tblCmpOsSubnetroutesMapper.updateByPrimaryKeySelective(tblCmpOsSubnetroutes);
	}

	public int insertDnsnameserver(TblCmpOsDnsnameservers tblCmpOsSubnetroutes)
	{
		return tblCmpOsDnsnameserversMapper.insert(tblCmpOsSubnetroutes);
	}

	public List<TblCmpOsDnsnameservers> getDnsnameservers(TblCmpOsDnsnameserversExample example)
	{
		return tblCmpOsDnsnameserversMapper.selectByExample(example);
	}

	public long countDnsnameserversByExample(TblCmpOsDnsnameserversExample example)
	{
		return tblCmpOsDnsnameserversMapper.countByExample(example);
	}

	public int updateDnsnameserver(TblCmpOsDnsnameservers tblCmpOsDnsnameservers)
	{
		return tblCmpOsDnsnameserversMapper.updateByPrimaryKeySelective(tblCmpOsDnsnameservers);
	}

	public int insertSubnetServiceType(TblCmpOsSubnetServiceTypes tblCmpOsSubnetServiceTypes)
	{
		return tblCmpOsSubnetServiceTypesMapper.insert(tblCmpOsSubnetServiceTypes);
	}

	public List<TblCmpOsSubnetServiceTypes> getSubnetServiceTypes(TblCmpOsSubnetServiceTypesExample example)
	{
		return tblCmpOsSubnetServiceTypesMapper.selectByExample(example);
	}

	public long countSubnetServiceTypesByExample(TblCmpOsSubnetServiceTypesExample example)
	{
		return tblCmpOsSubnetServiceTypesMapper.countByExample(example);
	}

	public int updateSubnetServiceType(TblCmpOsSubnetServiceTypes tblCmpOsSubnetServiceTypes)
	{
		return tblCmpOsSubnetServiceTypesMapper.updateByPrimaryKeySelective(tblCmpOsSubnetServiceTypes);
	}

	public TblCmpOsNetworks getNetworkById(String cloudId, String networkId)
	{
		return tblCmpOsNetworksMapper.selectByPrimaryKey(new TblCmpOsNetworksKey(cloudId, networkId));
	}

	public int insertNetwork(TblCmpOsNetworks tblCmpOsNetworks)
	{
		return tblCmpOsNetworksMapper.insert(tblCmpOsNetworks);
	}

	public List<TblCmpOsNetworks> getNetworks(TblCmpOsNetworksExample example)
	{
		return tblCmpOsNetworksMapper.selectByExample(example);
	}

	public long countNetworksByExample(TblCmpOsNetworksExample example)
	{
		return tblCmpOsNetworksMapper.countByExample(example);
	}

	public int updateNetwork(TblCmpOsNetworks tblCmpOsNetworks)
	{
		return tblCmpOsNetworksMapper.updateByPrimaryKeySelective(tblCmpOsNetworks);
	}

	public int deleteNetwork(String cloudId, String networkId)
	{
		TblCmpOsNetworks tblCmpOsNetwork = new TblCmpOsNetworks();
		tblCmpOsNetwork.setCloudId(cloudId);
		tblCmpOsNetwork.setId(networkId);
		tblCmpOsNetwork.setEeStatus(REMOVED);

		return updateNetwork(tblCmpOsNetwork);
	}

	public Set<String> getNetworkIds(String cloudId)
	{
		return tblCmpOsNetworksMapper.getNetworkIds(cloudId);
	}

	public List<TblCmpOsNetworks> getNetworksWithShared(TblCmpOsNetworksExample example, String projectId, String userId)
	{
		return tblCmpOsNetworksMapper.selectByExampleWithShared(example, projectId, userId);
	}

	public int insertNetworksegment(TblCmpOsNetworksegments tblCmpOsNetworksegments)
	{
		return tblCmpOsNetworksegmentsMapper.insert(tblCmpOsNetworksegments);
	}

	public List<TblCmpOsNetworksegments> getNetworksegments(TblCmpOsNetworksegmentsExample example)
	{
		return tblCmpOsNetworksegmentsMapper.selectByExample(example);
	}

	public long countNetworksegmentsByExample(TblCmpOsNetworksegmentsExample example)
	{
		return tblCmpOsNetworksegmentsMapper.countByExample(example);
	}

	public int updateNetworksegment(TblCmpOsNetworksegments tblCmpOsNetworksegments)
	{
		return tblCmpOsNetworksegmentsMapper.updateByPrimaryKeySelective(tblCmpOsNetworksegments);
	}

	public TblCmpOsPorts getPortById(String cloudId, String portId)
	{
		return tblCmpOsPortsMapper.selectByPrimaryKey(new TblCmpOsPortsKey(cloudId, portId));
	}

	public int insertPort(TblCmpOsPorts tblCmpOsPorts)
	{
		return tblCmpOsPortsMapper.insert(tblCmpOsPorts);
	}

	public List<TblCmpOsPorts> getPorts(TblCmpOsPortsExample example)
	{
		return tblCmpOsPortsMapper.selectByExample(example);
	}

	public long countPortsByExample(TblCmpOsPortsExample example)
	{
		return tblCmpOsPortsMapper.countByExample(example);
	}

	public int updatePort(TblCmpOsPorts tblCmpOsPorts)
	{
		return tblCmpOsPortsMapper.updateByPrimaryKeySelective(tblCmpOsPorts);
	}

	public int deletePort(String cloudId, String portId)
	{
		TblCmpOsPorts tblCmpOsPort = new TblCmpOsPorts();
		tblCmpOsPort.setCloudId(cloudId);
		tblCmpOsPort.setId(portId);
		tblCmpOsPort.setEeStatus(REMOVED);

		return updatePort(tblCmpOsPort);
	}

	public Set<String> getPortIds(String cloudId, String deviceId)
	{
		return tblCmpOsPortsMapper.getPortIds(cloudId, deviceId);
	}

	public List<TblCmpOsPorts> getPortsWithShared(TblCmpOsPortsExample example, String cloudId, String projectId, String userId)
	{
		return tblCmpOsPortsMapper.selectByExampleWithShared(example, cloudId, projectId, userId);
	}

	public int insertAllowedaddresspair(TblCmpOsAllowedaddresspairs tblCmpOsAllowedaddresspairs)
	{
		return tblCmpOsAllowedaddresspairsMapper.insert(tblCmpOsAllowedaddresspairs);
	}

	public List<TblCmpOsAllowedaddresspairs> getAllowedaddresspairs(TblCmpOsAllowedaddresspairsExample example)
	{
		return tblCmpOsAllowedaddresspairsMapper.selectByExample(example);
	}

	public long countAllowedaddresspairsByExample(TblCmpOsAllowedaddresspairsExample example)
	{
		return tblCmpOsAllowedaddresspairsMapper.countByExample(example);
	}

	public int updateAllowedaddresspair(TblCmpOsAllowedaddresspairs tblCmpOsAllowedaddresspairs)
	{
		return tblCmpOsAllowedaddresspairsMapper.updateByPrimaryKeySelective(tblCmpOsAllowedaddresspairs);
	}

	public TblCmpOsMl2PortBindings getMl2PortBindingById(String cloudId, String portId, String hostId)
	{
		return tblCmpOsMl2PortBindingsMapper.selectByPrimaryKey(new TblCmpOsMl2PortBindingsKey(cloudId, portId, hostId));
	}

	public int insertMl2PortBinding(TblCmpOsMl2PortBindings tblCmpOsMl2PortBindings)
	{
		return tblCmpOsMl2PortBindingsMapper.insert(tblCmpOsMl2PortBindings);
	}

	public List<TblCmpOsMl2PortBindings> getMl2PortBindings(TblCmpOsMl2PortBindingsExample example)
	{
		return tblCmpOsMl2PortBindingsMapper.selectByExample(example);
	}

	public long countMl2PortBindingsByExample(TblCmpOsMl2PortBindingsExample example)
	{
		return tblCmpOsMl2PortBindingsMapper.countByExample(example);
	}

	public int updateMl2PortBinding(TblCmpOsMl2PortBindings tblCmpOsMl2PortBindings)
	{
		return tblCmpOsMl2PortBindingsMapper.updateByPrimaryKeySelective(tblCmpOsMl2PortBindings);
	}

	public int insertMl2PortBindingLevel(TblCmpOsMl2PortBindingLevels tblCmpOsMl2PortBindingLevels)
	{
		return tblCmpOsMl2PortBindingLevelsMapper.insert(tblCmpOsMl2PortBindingLevels);
	}

	public List<TblCmpOsMl2PortBindingLevels> getMl2PortBindingLevels(TblCmpOsMl2PortBindingLevelsExample example)
	{
		return tblCmpOsMl2PortBindingLevelsMapper.selectByExample(example);
	}

	public long countMl2PortBindingLevelsByExample(TblCmpOsMl2PortBindingLevelsExample example)
	{
		return tblCmpOsMl2PortBindingLevelsMapper.countByExample(example);
	}

	public int updateMl2PortBindingLevel(TblCmpOsMl2PortBindingLevels tblCmpOsMl2PortBindingLevels)
	{
		return tblCmpOsMl2PortBindingLevelsMapper.updateByPrimaryKeySelective(tblCmpOsMl2PortBindingLevels);
	}

	public int insertMl2DistributedPortBinding(TblCmpOsMl2DistributedPortBindings tblCmpOsMl2DistributedPortBindings)
	{
		return tblCmpOsMl2DistributedPortBindingsMapper.insert(tblCmpOsMl2DistributedPortBindings);
	}

	public List<TblCmpOsMl2DistributedPortBindings> getMl2DistributedPortBindings(TblCmpOsMl2DistributedPortBindingsExample example)
	{
		return tblCmpOsMl2DistributedPortBindingsMapper.selectByExample(example);
	}

	public long countMl2DistributedPortBindingsByExample(TblCmpOsMl2DistributedPortBindingsExample example)
	{
		return tblCmpOsMl2DistributedPortBindingsMapper.countByExample(example);
	}

	public int updateMl2DistributedPortBinding(TblCmpOsMl2DistributedPortBindings tblCmpOsMl2DistributedPortBindings)
	{
		return tblCmpOsMl2DistributedPortBindingsMapper.updateByPrimaryKeySelective(tblCmpOsMl2DistributedPortBindings);
	}

	public int insertExtradhcpopt(TblCmpOsExtradhcpopts tblCmpOsExtradhcpopts)
	{
		return tblCmpOsExtradhcpoptsMapper.insert(tblCmpOsExtradhcpopts);
	}

	public List<TblCmpOsExtradhcpopts> getExtradhcpopts(TblCmpOsExtradhcpoptsExample example)
	{
		return tblCmpOsExtradhcpoptsMapper.selectByExample(example);
	}

	public long countExtradhcpoptsByExample(TblCmpOsExtradhcpoptsExample example)
	{
		return tblCmpOsExtradhcpoptsMapper.countByExample(example);
	}

	public int updateExtradhcpopt(TblCmpOsExtradhcpopts tblCmpOsExtradhcpopts)
	{
		return tblCmpOsExtradhcpoptsMapper.updateByPrimaryKeySelective(tblCmpOsExtradhcpopts);
	}

	public int insertSecuritygroupportbinding(TblCmpOsSecuritygroupportbindings tblCmpOsSecuritygroupportbindings)
	{
		return tblCmpOsSecuritygroupportbindingsMapper.insert(tblCmpOsSecuritygroupportbindings);
	}

	public List<TblCmpOsSecuritygroupportbindings> getSecuritygroupportbindings(TblCmpOsSecuritygroupportbindingsExample example)
	{
		return tblCmpOsSecuritygroupportbindingsMapper.selectByExample(example);
	}

	public long countSecuritygroupportbindingsByExample(TblCmpOsSecuritygroupportbindingsExample example)
	{
		return tblCmpOsSecuritygroupportbindingsMapper.countByExample(example);
	}

	public int updateSecuritygroupportbinding(TblCmpOsSecuritygroupportbindings tblCmpOsSecuritygroupportbindings)
	{
		return tblCmpOsSecuritygroupportbindingsMapper.updateByPrimaryKeySelective(tblCmpOsSecuritygroupportbindings);
	}

	public int insertIpallocation(TblCmpOsIpallocations tblCmpOsIpallocations)
	{
		return tblCmpOsIpallocationsMapper.insert(tblCmpOsIpallocations);
	}

	public List<TblCmpOsIpallocations> getIpallocations(TblCmpOsIpallocationsExample example)
	{
		return tblCmpOsIpallocationsMapper.selectByExample(example);
	}

	public long countIpallocationsByExample(TblCmpOsIpallocationsExample example)
	{
		return tblCmpOsIpallocationsMapper.countByExample(example);
	}

	public int updateIpallocation(TblCmpOsIpallocations tblCmpOsIpallocations)
	{
		return tblCmpOsIpallocationsMapper.updateByPrimaryKeySelective(tblCmpOsIpallocations);
	}

	public TblCmpOsFloatingips getFloatingipById(String cloudId, String floatingipId)
	{
		return tblCmpOsFloatingipsMapper.selectByPrimaryKey(new TblCmpOsFloatingipsKey(cloudId, floatingipId));
	}

	public int insertFloatingip(TblCmpOsFloatingips tblCmpOsFloatingips)
	{
		return tblCmpOsFloatingipsMapper.insert(tblCmpOsFloatingips);
	}

	public List<TblCmpOsFloatingips> getFloatingips(TblCmpOsFloatingipsExample example)
	{
		return tblCmpOsFloatingipsMapper.selectByExample(example);
	}

	public long countFloatingipsByExample(TblCmpOsFloatingipsExample example)
	{
		return tblCmpOsFloatingipsMapper.countByExample(example);
	}

	public int updateFloatingip(TblCmpOsFloatingips tblCmpOsFloatingips)
	{
		return tblCmpOsFloatingipsMapper.updateByPrimaryKeySelective(tblCmpOsFloatingips);
	}

	public int updateFloatingipEeSelective(TblCmpOsFloatingips tblCmpOsFloatingips)
	{
		return tblCmpOsFloatingipsMapper.updateByPrimaryKeyEeSelective(tblCmpOsFloatingips);
	}

	public int deleteFloatingIp(String cloudId, String floatingIpId)
	{
		TblCmpOsFloatingips tblCmpOsFloatingip = new TblCmpOsFloatingips();
		tblCmpOsFloatingip.setCloudId(cloudId);
		tblCmpOsFloatingip.setId(floatingIpId);
		tblCmpOsFloatingip.setEeStatus(REMOVED);

		return updateFloatingip(tblCmpOsFloatingip);
	}

	public Set<String> getFloatingipIds(String cloudId)
	{
		return tblCmpOsFloatingipsMapper.getFloatingipIds(cloudId);
	}

	public TblCmpOsPortforwardings getPortforwardingById(String cloudId, String portForwardingId)
	{
		return tblCmpOsPortforwardingsMapper.selectByPrimaryKey(new TblCmpOsPortforwardingsKey(cloudId, portForwardingId));
	}

	public int insertPortforwarding(TblCmpOsPortforwardings tblCmpOsPortforwardings)
	{
		return tblCmpOsPortforwardingsMapper.insert(tblCmpOsPortforwardings);
	}

	public List<TblCmpOsPortforwardings> getPortforwardings(TblCmpOsPortforwardingsExample example)
	{
		return tblCmpOsPortforwardingsMapper.selectByExample(example);
	}

	public long countPortforwardingsByExample(TblCmpOsPortforwardingsExample example)
	{
		return tblCmpOsPortforwardingsMapper.countByExample(example);
	}

	public int updatePortforwarding(TblCmpOsPortforwardings tblCmpOsPortforwardings)
	{
		return tblCmpOsPortforwardingsMapper.updateByPrimaryKeySelective(tblCmpOsPortforwardings);
	}

	public Set<String> getPortforwardingIds(String cloudId)
	{
		return tblCmpOsPortforwardingsMapper.getPortforwardingIds(cloudId);
	}

	public TblCmpOsRouters getRouterById(String cloudId, String routerId)
	{
		return tblCmpOsRoutersMapper.selectByPrimaryKey(new TblCmpOsRoutersKey(cloudId, routerId));
	}

	public int insertRouter(TblCmpOsRouters tblCmpOsRouters)
	{
		return tblCmpOsRoutersMapper.insert(tblCmpOsRouters);
	}

	public List<TblCmpOsRouters> getRouters(TblCmpOsRoutersExample example)
	{
		return tblCmpOsRoutersMapper.selectByExample(example);
	}

	public long countRoutersByExample(TblCmpOsRoutersExample example)
	{
		return tblCmpOsRoutersMapper.countByExample(example);
	}

	public int updateRouter(TblCmpOsRouters tblCmpOsRouters)
	{
		return tblCmpOsRoutersMapper.updateByPrimaryKeySelective(tblCmpOsRouters);
	}

	public int updateRouterEeSelective(TblCmpOsRouters tblCmpOsRouters)
	{
		return tblCmpOsRoutersMapper.updateByPrimaryKeyEeSelective(tblCmpOsRouters);
	}

	public int deleteRouter(String cloudId, String routerId)
	{
		TblCmpOsRouters tblCmpOsRouter = new TblCmpOsRouters();
		tblCmpOsRouter.setCloudId(cloudId);
		tblCmpOsRouter.setId(routerId);
		tblCmpOsRouter.setEeStatus(REMOVED);

		return updateRouter(tblCmpOsRouter);
	}

	public Set<String> getRouterIds(String cloudId)
	{
		return tblCmpOsRoutersMapper.getRouterIds(cloudId);
	}

	public List<TblCmpEsRouters> getRoutersWithFirewall(TblCmpOsRoutersExample example, String cloudId, String firewallId)
	{
		return tblCmpOsRoutersMapper.selectByExampleWithFirewall(example, cloudId, firewallId);
	}

	public int insertRouterroute(TblCmpOsRouterroutes tblCmpOsRouterroutes)
	{
		return tblCmpOsRouterroutesMapper.insert(tblCmpOsRouterroutes);
	}

	public List<TblCmpOsRouterroutes> getRouterroutes(TblCmpOsRouterroutesExample example)
	{
		return tblCmpOsRouterroutesMapper.selectByExample(example);
	}

	public long countRouterroutesByExample(TblCmpOsRouterroutesExample example)
	{
		return tblCmpOsRouterroutesMapper.countByExample(example);
	}

	public int updateRouterroute(TblCmpOsRouterroutes tblCmpOsRouterroutes)
	{
		return tblCmpOsRouterroutesMapper.updateByPrimaryKeySelective(tblCmpOsRouterroutes);
	}

	public int updateRouterrouteEeStatusByRouterid(String cloudId, String routerId, Integer eeStatus)
	{
		return tblCmpOsRouterroutesMapper.updateRouterrouteEeStatusByRouterid(cloudId, routerId, eeStatus);
	}

	public int insertRouterport(TblCmpOsRouterports tblCmpOsRouterports)
	{
		return tblCmpOsRouterportsMapper.insert(tblCmpOsRouterports);
	}

	public List<TblCmpOsRouterports> getRouterports(TblCmpOsRouterportsExample example)
	{
		return tblCmpOsRouterportsMapper.selectByExample(example);
	}

	public long countRouterportsByExample(TblCmpOsRouterportsExample example)
	{
		return tblCmpOsRouterportsMapper.countByExample(example);
	}

	public int updateRouterport(TblCmpOsRouterports tblCmpOsRouterports)
	{
		return tblCmpOsRouterportsMapper.updateByPrimaryKeySelective(tblCmpOsRouterports);
	}

	public int updateRouterportEeStatusByRouterid(String cloudId, String routerId, Integer eeStatus)
	{
		return tblCmpOsRouterportsMapper.updateRouterportEeStatusByRouterid(cloudId, routerId, eeStatus);
	}

	public int insertDvrHostMac(TblCmpOsDvrHostMacs tblCmpOsDvrHostMacs)
	{
		return tblCmpOsDvrHostMacsMapper.insert(tblCmpOsDvrHostMacs);
	}

	public List<TblCmpOsDvrHostMacs> getDvrHostMacs(TblCmpOsDvrHostMacsExample example)
	{
		return tblCmpOsDvrHostMacsMapper.selectByExample(example);
	}

	public long countDvrHostMacsByExample(TblCmpOsDvrHostMacsExample example)
	{
		return tblCmpOsDvrHostMacsMapper.countByExample(example);
	}

	public int updateDvrHostMac(TblCmpOsDvrHostMacs tblCmpOsDvrHostMacs)
	{
		return tblCmpOsDvrHostMacsMapper.updateByPrimaryKeySelective(tblCmpOsDvrHostMacs);
	}

	public TblCmpOsSecuritygroups getSecuritygroupById(String cloudId, String securityGroupId)
	{
		return tblCmpOsSecuritygroupsMapper.selectByPrimaryKey(new TblCmpOsSecuritygroupsKey(cloudId, securityGroupId));
	}

	public int insertSecuritygroup(TblCmpOsSecuritygroups tblCmpOsSecuritygroups)
	{
		return tblCmpOsSecuritygroupsMapper.insert(tblCmpOsSecuritygroups);
	}

	public List<TblCmpOsSecuritygroups> getSecuritygroups(TblCmpOsSecuritygroupsExample example)
	{
		return tblCmpOsSecuritygroupsMapper.selectByExample(example);
	}

	public long countSecuritygroupsByExample(TblCmpOsSecuritygroupsExample example)
	{
		return tblCmpOsSecuritygroupsMapper.countByExample(example);
	}

	public int updateSecuritygroup(TblCmpOsSecuritygroups tblCmpOsSecuritygroups)
	{
		return tblCmpOsSecuritygroupsMapper.updateByPrimaryKeySelective(tblCmpOsSecuritygroups);
	}

	public int deleteSecurityGroup(String cloudId, String securityGroupId)
	{
		TblCmpOsSecuritygroups tblCmpOsSecuritygroup = new TblCmpOsSecuritygroups();
		tblCmpOsSecuritygroup.setCloudId(cloudId);
		tblCmpOsSecuritygroup.setId(securityGroupId);
		tblCmpOsSecuritygroup.setEeStatus(REMOVED);

		return updateSecuritygroup(tblCmpOsSecuritygroup);
	}

	public Set<String> getSecuritygroupIds(String cloudId)
	{
		return tblCmpOsSecuritygroupsMapper.getSecuritygroupIds(cloudId);
	}

	public List<TblCmpOsSecuritygroups> getTblCmpSecuritygroupsByServer(String cloudId, String instanceUuid)
	{
		return tblCmpOsSecuritygroupsMapper.getTblCmpSecuritygroupsByServer(cloudId, instanceUuid);
	}

	public TblCmpOsSecuritygrouprules getSecuritygroupruleById(String cloudId, String securityGroupRuleId)
	{
		return tblCmpOsSecuritygrouprulesMapper.selectByPrimaryKey(new TblCmpOsSecuritygrouprulesKey(cloudId, securityGroupRuleId));
	}

	public int insertSecuritygrouprule(TblCmpOsSecuritygrouprules tblCmpOsSecuritygrouprules)
	{
		return tblCmpOsSecuritygrouprulesMapper.insert(tblCmpOsSecuritygrouprules);
	}

	public List<TblCmpOsSecuritygrouprules> getSecuritygrouprules(TblCmpOsSecuritygrouprulesExample example)
	{
		return tblCmpOsSecuritygrouprulesMapper.selectByExample(example);
	}

	public long countSecuritygrouprulesByExample(TblCmpOsSecuritygrouprulesExample example)
	{
		return tblCmpOsSecuritygrouprulesMapper.countByExample(example);
	}

	public int updateSecuritygrouprule(TblCmpOsSecuritygrouprules tblCmpOsSecuritygrouprules)
	{
		return tblCmpOsSecuritygrouprulesMapper.updateByPrimaryKeySelective(tblCmpOsSecuritygrouprules);
	}

	public int deleteSecurityGroupRule(String cloudId, String securityGroupRuleId)
	{
		TblCmpOsSecuritygrouprules tblCmpOsSecuritygrouprule = new TblCmpOsSecuritygrouprules();
		tblCmpOsSecuritygrouprule.setCloudId(cloudId);
		tblCmpOsSecuritygrouprule.setId(securityGroupRuleId);
		tblCmpOsSecuritygrouprule.setEeStatus(REMOVED);

		return updateSecuritygrouprule(tblCmpOsSecuritygrouprule);
	}

	public Set<String> getSecuritygroupruleIds(String cloudId)
	{
		return tblCmpOsSecuritygrouprulesMapper.getSecuritygroupruleIds(cloudId);
	}

	public Set<String> getSecuritygroupruleIds(String cloudId, String securityGroupId)
	{
		return tblCmpOsSecuritygrouprulesMapper.getSecuritygroupruleIdsBySecurityGroup(cloudId, securityGroupId);
	}

	public Set<String> getSubnetConnectToRouter(String cloudId)
	{
		return manualMapper.getSubnetConnectToRouter(cloudId);
	}
}
