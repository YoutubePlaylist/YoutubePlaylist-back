package com.example.youtubedb.util;

import com.example.youtubedb.exception.ContractViolationException;

import static com.example.youtubedb.util.Message.PLEASE_CHECK;

public class ContractUtil {
	public static void requires(boolean expectedToBeTrue, Message message) {
		if(expectedToBeTrue) {
			return;
		}

		throw new ContractViolationException(message.name());
	}

	public static void requires(boolean expectedToBeTrue) {
		requires(expectedToBeTrue, PLEASE_CHECK);
	}
}
