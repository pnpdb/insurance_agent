package com.gecq.insurance.agent.service.utils;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

@Component("encryptionUtils")
public class EncryptionUtils {
	private static final int SALT_LENGTH = 12;
	private static final String HEX_NUMS_STR = "0123456789abcdef";

	/**
	 * 将16进制字符串转换成字节数组
	 *
	 * @param hex 16进制字符串
	 * @return 16进制字节数组
	 */
	private byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] hexChars = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (HEX_NUMS_STR.indexOf(hexChars[pos]) << 4 | HEX_NUMS_STR
					.indexOf(hexChars[pos + 1]));
		}
		return result;
	}

	/**
	 * 将指定byte数组转换成16进制字符串
	 *
	 * @param b 字节
	 * @return 16进制字符串
	 */
	public String byteToHexString(byte[] b) {
		StringBuilder hexString = new StringBuilder();
		for (byte c : b) {
			String hex = Integer.toHexString(c & 0x000000ff | 0xffffff00)
					.substring(6);
			hexString.append(hex.toLowerCase());
		}
		return hexString.toString();
	}

	/**
	 * 验证口令是否合法
	 *
	 * @param password 明文密码
	 * @param passwordInDb 加密密码
	 * @return 是否合法boolean
	 */
	public boolean validEncryptedChar(String password, String passwordInDb,
									  String salt){
		// 将16进制字符串格式口令转换成字节数组
		byte[] pwdInDb = hexStringToByte(passwordInDb);
		// 声明盐变量
		byte[] sal = hexStringToByte(salt);
		// 创建消息摘要对象
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		}
		// 将盐数据传入消息摘要对象
		md.update(sal);
		// 将口令的数据传给消息摘要对象
		try {
			md.update(password.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 生成输入口令的消息摘要
		byte[] digest = md.digest();
		// 比较根据输入口令生成的消息摘要和数据库中消息摘要是否相同
		return Arrays.equals(digest, pwdInDb);
	}

	public String getEncryptedChar(String password,String salt) {
		return getEncryptedChar(password, hexStringToByte(salt));
	}

	/**
	 * 获得加密后的16进制形式口令
	 *
	 * @param password 密码
	 * @return 加密后的密码
	 */
	public String getEncryptedChar(String password, byte[] salt) {
		// 声明消息摘要对象
		MessageDigest md;
		// 创建消息摘要
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		// 将盐数据传入消息摘要对象
		md.update(salt);
		// 将口令的数据传给消息摘要对象
		try {
			md.update(password.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 获得消息摘要的字节数组
		byte[] digest = md.digest();
		// 将字节数组格式加密后的口令转化为16进制字符串格式的口令
		return byteToHexString(digest);
	}

	public byte[] nextSalt() {
		// 随机数生成器
		SecureRandom random = new SecureRandom();
		// 声明盐数组变量
		byte[] salt = new byte[SALT_LENGTH];
		// 将随机数放入盐变量中
		random.nextBytes(salt);
		return salt;
	}
}
