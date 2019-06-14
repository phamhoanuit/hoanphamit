package com.example.hometest.model;


import java.util.ArrayList;
import java.util.List;

public class DataModel {
    private ILoadData listener;
    int countCharacterStart = 0;
    int countCharacterEnd = 0;
    String splitStart = "";
    String splitEnd = "";
    List<String> dataReturn = new ArrayList<>();

    public DataModel(ILoadData listener) {
        this.listener = listener;
    }

    public void createData(){
        processData(getStringData());

        listener.getData(dataReturn);
    }

    /**
     * add data
     * @return
     */
    private List<String> getStringData(){
        List<String> data = new ArrayList<>();
        data.add("xiaomi");
        data.add("bts");
        data.add("balo");
        data.add("bitis hunter x");
        data.add("tai nghe");
        data.add("harry potter");
        data.add("anker");
        data.add("iphone");
        data.add("balo nữ");
        data.add("nguyễn nhật ánh");
        data.add("đắc nhân tâm");
        data.add("ipad");
        data.add("senka");
        data.add("tai nghe bluetooth");
        data.add("son");
        data.add("maybelline");
        data.add("laneige");
        data.add("kem chống nắng");
        data.add("anh chính là thanh xuân của em");
        return data;
    }

    /**
     *
     * @param data
     */
    private void processData(List<String> data) {
        for (String value : data) {
            checkValue(value.trim());
        }
    }

    /**
     * count space
     * @param value
     * @return
     */
    private int countSpace(String value) {
        int count = 0;
        char kyTu;
        for (int i = 0; i < value.length(); i++) {
            kyTu = value.charAt(i);
            if (Character.isWhitespace(kyTu)) {
                count++;
            }
        }
        return count;
    }

    /**
     * check value have contains space
     * @param value
     */
    private void checkValue(String value) {
        if (value.contains(" ")) {
            if (countSpace(value) == 1) {
                dataReturn.add(replaceSpace(value));
            } else {
                dataReturn.add(processValue(value));
            }
        } else {
            dataReturn.add(value);
        }
    }

    /**
     * replace \n to space
     * @param value
     * @return
     */
    private String replaceSpace(String value) {
        String val = "";
        char[] val1 = value.toCharArray();
        for (int i = 0; i < value.length(); i++) {
            if (Character.toString(val1[i]).equals(" ")) {
                val = new StringBuffer(value).replace(i, i+1, "\n").toString();
            }
        }
        return val;
    }

    /**
     * replace \n to space
     * @param value
     * @return
     */
    private String processValue(String value) {
        int countEnd = 0;
        int countStart = 0;
        String val = "";
        char[] val1 = value.toCharArray();
        int length = value.length() / 2;

        if (Character.toString(val1[length]).equals(" ")) {
            val = new StringBuffer(value).replace(length, length, "\n").toString();
        } else {
            countEnd = countCharacter(subStringEnd(val1, length, value));
            countStart = countCharacter(subStringStart(val1, length - 1, value));
            System.out.println("value: " + value
                    + "\n length" + value.length()
                    + "\n countEnd: " + countEnd + "\n countStart: " + countStart
                    + "\n countCharacterEnd: " + countCharacterEnd + "\n countCharacterStart: " + countCharacterStart
                    + "\n ------------------------------------------------------------");
            if (countEnd == countStart) {
                val = new StringBuffer(value).replace(countCharacterEnd, countCharacterEnd, "\n").toString();
            } else if (countEnd > countStart) {
                val = new StringBuffer(value).replace(countCharacterEnd, countCharacterEnd, "\n").toString();
            } else {
                val = new StringBuffer(value).replace(countCharacterStart, countCharacterStart + 1, "\n").toString();
            }
        }
        return val;
    }

    /**
     * count length String
     * @param val
     * @return
     */
    private int countCharacter(String val) {
        return val.length();
    }

    /**
     * sub string from middle to end
     * @param value
     * @param length
     * @param val
     * @return
     */
    private String subStringEnd(char[] value, int length, String val) {
        for (int i = length; i < val.length(); i++) {
            if (Character.toString(value[i]).equals(" ")) {
                splitEnd = val.substring(i + 1, value.length);
                countCharacterEnd = ++i;
                break;
            }
        }
        return splitEnd;
    }

    /**
     * sub String from start to middle
     * @param value
     * @param length
     * @param val
     * @return
     */
    private String subStringStart(char[] value, int length, String val) {
        for (int i = length; i > 0; i--) {
            if (Character.toString(value[i]).equals(" ")) {
                splitStart = val.substring(0, i);
                countCharacterStart = i;
                break;
            }
        }
        return splitStart;
    }
}
