package com.lnjoying.justice.aos.domain.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DockerComposeService
{
	String image;
	Boolean tty;
	Map<String,String> environment;
	List<String> links;
	List<String> volumes_from;
	List<String> ports;
	Map<String, String> labels;
	Boolean stdin_open;
	String network_mode;
	List<String> volumes;
	List<String> command;
	List<String> configs;
}
