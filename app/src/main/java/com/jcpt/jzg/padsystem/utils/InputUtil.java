package com.jcpt.jzg.padsystem.utils;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.EditText;

import com.blankj.utilcode.utils.StringUtils;
import com.jcpt.jzg.padsystem.interfaces.AfterTextChanged;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zealjiang on 2017/4/18 16:38.
 * Email: zealjiang@126.com
 */

public class InputUtil {

    private static InputUtil instance;

    private InputUtil(){}

    public static InputUtil getInstance(){
        if(instance==null){
            instance = new InputUtil();
            return instance;
        }
        return instance;
    }

    /**
     * 输入限止  对EditText限止输入 true表示限制,false表示不限制
     * @author zealjiang
     * @time 2017/2/24 10:48
     * @param editText
     * @param face  表情
     * @param chinese 中文
     * @param number  数字
     * @param english 英文
     */
    public static void inputRestrict(final EditText editText, boolean face, boolean chinese, boolean number, boolean english){

        String sFace = ".*[\ud83c\udc00-\ud83c\udfff].*|.*[\ud83d\udc00-\ud83d\udfff].*|.*[\u2600-\u27ff].*";
        String sChinise = ".*[\u4e00-\u9fa5].*";
        String sNumber = ".*[0-9].*";
        String sEnglish = ".*[a-zA-Z].*";
        StringBuilder sRestric = new StringBuilder();
        StringBuilder sHint = new StringBuilder();
        if(face){
            sRestric.append(sFace);
            sHint.append("不可输入表情符");
        }
        if(chinese){
            sRestric = sRestric.length()>0 ? sRestric.append("|").append(sChinise) : sRestric.append(sChinise);
            sHint = sHint.length()>0 ? sHint.append("、").append("汉字") : sHint.append("不可输入汉字");
        }
        if(number){
            sRestric = sRestric.length()>0 ? sRestric.append("|").append(sNumber) : sRestric.append(sNumber);
            sHint = sHint.length()>0 ? sHint.append("、").append("数字") : sHint.append("不可输入数字");
        }
        if(english){
            sRestric = sRestric.length()>0 ? sRestric.append("|").append(sEnglish) : sRestric.append(sEnglish);
            sHint = sHint.length()>0 ? sHint.append("、").append("英文字母") : sHint.append("不可输入英文字母");
        }

        final String restric = sRestric.toString();
        final String hint = sHint.toString();

        editText.addTextChangedListener(new TextWatcher() {
            String beforeText = "";
            String changedText = "";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //LogUtil.e("onTextChanged string","start :"+start+"  before: "+before+"  count: "+count);
                //增加字符
                if(before==0&&count>0) {
                    changedText = s.subSequence(start, start + count).toString();
                }else{
                    changedText = "";
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(changedText.equals("")){
                    return;
                }
                //LogUtil.e("afterTextChanged changedText",changedText);
                Pattern p = Pattern.compile(restric);
                Matcher m = p.matcher(changedText);
                if(m.matches()){
                    editText.removeTextChangedListener(this);
                    editText.setText(beforeText);
                    editText.setSelection(editText.getText().length());
                    MyToast.showShort(hint);
                    editText.addTextChangedListener(this);
                }
            }
        });

    }



    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock
                .CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock
                .CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock
                .GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * Created by 李波 on 2017/8/24.
     * 判断输入英文
     */
    public static boolean isABC(char c){

        char[] abc ={
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
        };

        for (int i = 0; i < abc.length; i++) {
            if (String.valueOf(abc[i]).equals(String.valueOf(c))){
                return true;
            }
        }

        return false;
    }

    /**
     * Created by 李波 on 2017/8/24.
     * 判断输入符号
     */
    public static boolean isSymbol(char c){

        String speChat="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“'。，、？]";
        Pattern pattern = Pattern.compile(speChat);
        Matcher matcher = pattern.matcher(String.valueOf(c));
        if(matcher.find()) //如果是符号返回true
            return true;

        return false;
    }



    /**
     * Created by 李波 on 2017/8/24.
     * EditText输入过滤器
     * 只能输入中文和英文，过滤掉其他。
     */
    public static void editTextfilter(EditText editText){
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int
                    dstart, int dend) {
                for (int i = start; i < end; i++) {
                    //如果不是中文和英文 或者 是符号 就禁止输入  -> 李波 on 2017/8/24.
                    if (!isChinese(source.charAt(i))&!isABC(source.charAt(i))||isSymbol(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(20)});
    }

    /**
     * 输入限止  对EditText限止输入 true表示限制,false表示不限制
     * @author zealjiang
     * @time 2017/2/24 10:48
     * @param editText
     * @param face  表情
     * @param chinese 中文
     * @param number  数字
     * @param english 英文
     * @param limitLetters 限制输入的字母 如不能输入字母ioIO
     */
    public void inputRestrict(final EditText editText,boolean face,boolean chinese,boolean number,boolean english,String limitLetters,final AfterTextChanged afterTextChanged){

        String sFace = ".*[\ud83c\udc00-\ud83c\udfff].*|.*[\ud83d\udc00-\ud83d\udfff].*|.*[\u2600-\u27ff].*";
        String sChinise = ".*[\u4e00-\u9fa5].*";
        String sNumber = ".*[0-9].*";
        String sEnglish = ".*[a-zA-Z].*";
        StringBuilder sRestric = new StringBuilder();
        StringBuilder sHint = new StringBuilder();
        if(face){
            sRestric.append(sFace);
            sHint.append("不可输入表情符");
        }
        if(chinese){
            sRestric = sRestric.length()>0 ? sRestric.append("|").append(sChinise) : sRestric.append(sChinise);
            sHint = sHint.length()>0 ? sHint.append("、").append("汉字") : sHint.append("不可输入汉字");
        }
        if(number){
            sRestric = sRestric.length()>0 ? sRestric.append("|").append(sNumber) : sRestric.append(sNumber);
            sHint = sHint.length()>0 ? sHint.append("、").append("数字") : sHint.append("不可输入数字");
        }
        if(english){
            sRestric = sRestric.length()>0 ? sRestric.append("|").append(sEnglish) : sRestric.append(sEnglish);
            sHint = sHint.length()>0 ? sHint.append("、").append("英文字母") : sHint.append("不可输入英文字母");
        }

        if(!StringUtils.isEmpty(limitLetters)){
            char[] chars = limitLetters.toCharArray();

            StringBuilder sbRegularLimit = new StringBuilder();
            StringBuilder sbHint = new StringBuilder();
            for (int i = 0; i < chars.length; i++) {
                sbRegularLimit.append(".*"+chars[i]+".*|");

                if(!sbHint.toString().toUpperCase().contains(String.valueOf(chars[i]).toUpperCase())) {
                    sbHint.append(String.valueOf(chars[i]).toUpperCase() + "  ");
                }
            }
            sbRegularLimit.deleteCharAt(sbRegularLimit.length()-1);
            sbHint.deleteCharAt(sbHint.length()-1);

            String sLetter = sbHint.toString();
            String regularLimit = sbRegularLimit.toString();
            if(!StringUtils.isEmpty(regularLimit)){
                sRestric = sRestric.length()>0 ? sRestric.append("|").append(regularLimit) : sRestric.append(regularLimit);
                sHint = sHint.length()>0 ? sHint.append("、").append(sLetter) : sHint.append("不可输入"+sLetter);
            }
        }


        final String restric = sRestric.toString();
        final String hint = sHint.toString();

        editText.addTextChangedListener(new TextWatcher() {
            String beforeText = "";
            String changedText = "";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //LogUtil.e("onTextChanged string","start :"+start+"  before: "+before+"  count: "+count);
                //增加字符
                if(before==0&&count>0) {
                    changedText = s.subSequence(start, start + count).toString();
                }else{
                    changedText = "";
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(changedText.equals("")){
                    return;
                }
                //LogUtil.e("afterTextChanged changedText",changedText);
                Pattern p = Pattern.compile(restric);
                Matcher m = p.matcher(changedText);
                if(m.matches()){
                    editText.removeTextChangedListener(this);
                    editText.setText(beforeText);
                    editText.setSelection(editText.getText().length());
                    MyToast.showShort(hint);
                    editText.addTextChangedListener(this);
                }

                if(afterTextChanged!=null){
                    afterTextChanged.afterTextChanged(s);
                }

            }
        });

    }
}
