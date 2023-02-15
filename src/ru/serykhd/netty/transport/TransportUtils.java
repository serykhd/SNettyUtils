package ru.serykhd.netty.transport;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class TransportUtils {

	@Getter
	public TransportType bestType = Arrays.stream(TransportType.values()).filter(TransportType::isAviable).findFirst().get();

	static {
		//System.out.println(PlatformDependent.normalizedOs().equals("linux"));
		//System.out.println(PlatformDependent.normalizedArch().equals("x86_64"));
	}

	public TransportType bestType() {
		return bestType;
	}
}
