// Modified or written by Luca Marrocco for inclusion with airbrake.
// Copyright (c) 2009 Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package airbrake.stacktrace;

import static java.text.MessageFormat.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import airbrake.NoticeXml;

public class iOSBacktraceLine implements BacktraceLine {

	private String crashLocation;
	private String memAddress;
	private String command;
	private String offset;
	private Boolean showMemAddress = true;
	
	private static Pattern p = Pattern.compile("^[0-9]+[ ]*([^ ]+)[ ]+([^ ]+)[ ]+((?:.(?!\\+ [0-9]+$))+) \\+ ([0-9]+)$");
	
	public iOSBacktraceLine() {}
	
	public iOSBacktraceLine(Boolean showMemAddress) {
		this.showMemAddress = showMemAddress;
	}
	
	public iOSBacktraceLine(String line) {
		this.acceptLine(line);
	}

	public BacktraceLine acceptLine(String line) {
		Matcher m = p.matcher(line);
		Boolean b = m.matches();
		if(b && m.groupCount() == 4) {
			this.crashLocation = m.group(1);
			this.memAddress = m.group(2);
			this.command = m.group(3);
			this.offset = m.group(4);
		} else {
			//throw new Exception("Bad iOS stacktrace line to parse: " + line);
		}
		
		return this;
	}
	
	private String toBacktrace(final String crashLocation, final String memAddress, final String command, final String offset) {
		String s = null;
		if(showMemAddress) {
			s = format("{0} {1} {2} + {3}", crashLocation, memAddress, command, offset);
		} else {
			s = format("{0} {1} + {2}", crashLocation, command, offset);
		}
		
		return s;
	}

	@Override
	public String toString() {
		return toBacktrace(crashLocation, memAddress, command, offset);
	}

	@Override
	public String toXml() {
		String attrs = null;
		if(showMemAddress) {
			attrs = format("method=\"{0} {1} + {2}\" file=\"{3}\" number=\"\"", NoticeXml.escapeXml(memAddress), NoticeXml.escapeXml(command), NoticeXml.escapeXml(offset), NoticeXml.escapeXml(crashLocation));
		} else {
			attrs = format("method=\"{0} + {1}\" file=\"{2}\" number=\"\"", NoticeXml.escapeXml(command), NoticeXml.escapeXml(offset), NoticeXml.escapeXml(crashLocation));
		}
		
		return format("<line {0}/>", attrs);
	}
}