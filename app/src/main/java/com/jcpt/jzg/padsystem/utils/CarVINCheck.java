package com.jcpt.jzg.padsystem.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 1.6	VIN校验功能
	需求描述：
	用户在手续信息界面填写VIN时，对VIN进行校验；
	实现方法：
	校验VIN字符，只允许英文及数字；
	VIN中的英文字母，需锁定大写；
	WIN总长度为17个字符；
	VIN中不允许包含I/O/Q；
	通过校验位校验VIN填写的正确性：
	第9位为校验位，为1-9任意一位数字或“X”；
	VIN中字母需转换为数字，具体对应关系如下：
VIN字母		A	B	C	D	E	F	G	H	J	K	L	M	N	P	R	S	T	U	V	W	X	Y	Z
对应数字	1	2	3	4	5	6	7	8	1	2	3	4	5	7	9	2	3	4	5	6	7	8	9
	17位VIN，每一位均对应一位加权数，具体对应关系如下：
位数	1	2	3	4	5	6	7	8	9	10	11	12	13	14	15	16	17
权数	8	7	6	5	4	3	2	10	校验位	9	8	7	6	5	4	3	2
	计算方法——将VIN中每一位字母或数字的对应数值乘以该位的加权系数，然后除以11，余数即为校验位，若余数为10，校验位为“X”；
	正常操作步骤
	用户填写VIN，点击手续信息“下一步按钮”，或切换app顶部各检测环节按钮时，对VIN进行校验；
	在1.0.1校验之后使用第九位校验位校验VIN，如用户填写有误，需进行系统提示，但不阻塞下一步；
	提示文字“根据VIN校验规则，可能存在填写错误，请仔细核对！”
	异常操作
	无
 * 
 * 车辆VIN校验
 * @author zealjiang
 *
 */
public class CarVINCheck {

	private String[] letters = {"A","B","C","D","E","F","G","H","J","K","L","M","N","P","R","S","T","U","V","W","X","Y","Z"};
	private int[] values = {1,2,3,4,5,6,7,8,1,2,3,4,5,7,9,2,3,4,5,6,7,8,9};
	private int[] weights = {8,7,6,5,4,3,2,10,0,9,8,7,6,5,4,3,2};

	private static CarVINCheck instance;

	private CarVINCheck(){}

	public static CarVINCheck getInstance(){
		if(instance==null){
			instance = new CarVINCheck();
			return instance;
		}
		return instance;
	}

	public void test(){
		String VIN = "LS5A2ABE16B116217";
		String VIN2 = "LSVFF66R8C2116280";
		String VIN3 = "LSGJV52P84S244832";
/*		String numVIN = vinToNum(VIN);
		int weightSum = calculateWeightSum(numVIN);
		String remainder = calculateRemainder(weightSum);*/
		boolean isValid = isVINValid(VIN3);
		System.out.println(isValid);
		
	}
	/**
	 * 将VIN转成数字串
	 * @param vin
	 */
	private String vinToNum(String vin){
		StringBuilder sb = new StringBuilder();
		String UpperCVin = vin.toUpperCase();
		char[] chars = UpperCVin.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if(isNumeric(String.valueOf(chars[i]))){
				sb.append(String.valueOf(chars[i])+"");
				continue;
			}
			for (int j = 0; j < letters.length; j++) {
				if(String.valueOf(chars[i]).equals(letters[j])){
					sb.append(values[j]+"");
					break;
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 判断是不是数字
	 * @param str
	 * @return
	 */
	private boolean isNumeric(String str){
	   Pattern pattern = Pattern.compile("[0-9]"); 
	   Matcher isNum = pattern.matcher(str);
	   if(!isNum.matches()){
	       return false; 
	   } 
	   return true; 
	}
	
	/**
	 * 计算权重累加和
	 * @param numVIN
	 * @return
	 */
	private int calculateWeightSum(String numVIN){
		int sum = 0;
		char[] chars = numVIN.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			//System.out.println(Integer.valueOf(String.valueOf(chars[i]))+"*"+weights[i]);
			sum += Integer.valueOf(String.valueOf(chars[i]))*weights[i];
		}
		return sum;
	}
	
	/**
	 * 计算余数
	 */
	private String calculateRemainder(int num){
		int remainder = num%11;
		if(remainder==10){
			return "X";
		}
		return remainder+"";
	}
	
	/**
	 * 判断是否是合法的VIN
	 * @param vin
	 * @return 合法返回true，反之返回false
	 */
	public boolean isVINValid(String vin){
		if(vin==null||vin.length()!=17){
			return false;
		}
		String UpperCVin = vin.toUpperCase();
		if(UpperCVin.contains("I")||UpperCVin.contains("O")||UpperCVin.contains("Q")){
			return false;
		}
/*		String numVIN = vinToNum(UpperCVin);
		int weightSum = calculateWeightSum(numVIN);
		String remainder = calculateRemainder(weightSum);
		if(String.valueOf(UpperCVin.charAt(8)).equals(remainder)){
			return true;
		}else{
			return false;
		}*/
		return true;
	}
}
