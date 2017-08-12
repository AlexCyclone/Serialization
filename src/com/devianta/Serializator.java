package com.devianta;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Serializator {

    private File file;
    private Map<Class<?>, Method[]> serializable = new HashMap<>();
    private boolean hasFile = false;

    public Serializator() {
        fillSerializable();
    }

    public Serializator(File file) {
        setFile(file);
        fillSerializable();
    }

    public void setFile(File file) {
        this.file = file;
        hasFile = true;
    }

    // Serialization

    public String serialize(Object obj) {
        Class<?> cls = (Class<?>) obj.getClass();
        String str = "";

        while (cls != Object.class) {
            Field[] flds = cls.getDeclaredFields();
            ArrayList<Field> savedFlds = new ArrayList<>();

            for (Field fld : flds) {
                if (fld.isAnnotationPresent(Save.class)) {
                    savedFlds.add(fld);
                }
            }

            str = savedFlds.isEmpty() ? str : str + serialize(obj, savedFlds) + ";";
            cls = cls.getSuperclass();
        }

        str = str.substring(0, str.length() - 1);

        if (hasFile) {
            FileOperation.stringWrite(str, file);
        }

        return str;
    }

    private String serialize(Object obj, ArrayList<Field> savedFlds) {
        StringBuffer sb = new StringBuffer();
        try {
            for (Field fld : savedFlds) {
                if (Modifier.isPrivate(fld.getModifiers())) {
                    fld.setAccessible(true);
                }

                String strFld;
                if (isSerializable(fld)) {
                    strFld = serializeField(fld, obj);
                } else {
                    Object o = fld.get(obj);
                    strFld = serialize(o);
                }

                sb.append(!strFld.isEmpty() ? strFld + ";" : "");
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return sb.substring(0, sb.length() - 1).toString();
    }

    private boolean isSerializable(Field fld) {
        Class<?> cls = fld.getType();
        if (serializable.containsKey(cls)) {
            return true;
        }
        return false;
    }

    private String serializeField(Field fld, Object obj) {
        try {
            Method get = serializable.get(fld.getType())[0];
            return fld.getName() + "=" + get.invoke(fld, obj);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return "";
    }

    // Deserialization

    public Object deserialize(Object obj) {
        String str = "";

        if (hasFile) {
            str = FileOperation.stringRead(file);
        }

        if (str.trim().equals("")) {
            return null;
        }

        ArrayList<String> strFlds = new ArrayList<>(Arrays.asList(str.split("[;]")));
        return deserialize(strFlds, obj);
    }

    private Object deserialize(ArrayList<String> strFlds, Object obj) {
        Class<?> cls = obj.getClass();

        while (cls != Object.class) {
            Field[] flds = cls.getDeclaredFields();
            ArrayList<Field> savedFlds = new ArrayList<>();

            for (Field fld : flds) {
                if (fld.isAnnotationPresent(Save.class)) {
                    savedFlds.add(fld);
                }
            }

            deserialize(strFlds, savedFlds, obj);

            cls = cls.getSuperclass();
        }

        return obj;
    }

    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    private void deserialize(ArrayList<String> strFlds, ArrayList<Field> savedFlds, Object obj) {
        try {
            for (Field fld : savedFlds) {
                if (Modifier.isPrivate(fld.getModifiers())) {
                    fld.setAccessible(true);
                }

                if (isSerializable(fld)) {
                    deserializeField(strFlds, fld, obj);
                } else {
                    Object o;
                    o = fld.get(obj);
                    deserialize(strFlds, o);
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
            e.printStackTrace();
        }
    }

    private void deserializeField(ArrayList<String> strFlds, Field fld, Object obj) {
        String[] strFld = strFlds.get(0).split("[=]", 2);
        strFlds.remove(0);

        if (strFld[1].equals("null")) {
            return;
        }

        if (!strFld[0].equals(fld.getName())) {
            throw new IllegalArgumentException("Unexpected argument " + strFld[0]);
        }

        try {
            Method get = serializable.get(fld.getType())[1];
            if (fld.getType() == String.class) {
                get.invoke(fld, obj, strFld[1]);
            } else if (fld.getType() == char.class) {
                get.invoke(fld, obj, strFld[1].charAt(0));
            } else {
                Method conv = serializable.get(fld.getType())[2];
                get.invoke(fld, obj, conv.invoke(null, strFld[1]));
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void fillSerializable() {
        try {
            fillParsered();
            fillOther();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Incorrect method");
        }
    }

    private void fillParsered() throws NoSuchMethodException {
        Class<?> f = Field.class;

        serializable.put(byte.class, new Method[] { f.getMethod("getByte", Object.class),
                f.getMethod("setByte", Object.class, byte.class),
                Byte.class.getMethod("parseByte", String.class) });
        serializable.put(short.class, new Method[] { f.getMethod("getShort", Object.class),
                f.getMethod("setShort", Object.class, short.class),
                Short.class.getMethod("parseShort", String.class) });
        serializable.put(int.class, new Method[] { f.getMethod("getInt", Object.class),
                f.getMethod("setInt", Object.class, int.class),
                Integer.class.getMethod("parseInt", String.class) });
        serializable.put(long.class, new Method[] { f.getMethod("getLong", Object.class),
                f.getMethod("setLong", Object.class, long.class),
                Long.class.getMethod("parseLong", String.class) });
        serializable.put(float.class, new Method[] { f.getMethod("getFloat", Object.class),
                f.getMethod("setFloat", Object.class, float.class),
                Float.class.getMethod("parseFloat", String.class) });
        serializable.put(double.class, new Method[] { f.getMethod("getDouble", Object.class),
                f.getMethod("setDouble", Object.class, double.class),
                Double.class.getMethod("parseDouble", String.class) });
        serializable.put(boolean.class, new Method[] { f.getMethod("getBoolean", Object.class),
                f.getMethod("setBoolean", Object.class, boolean.class),
                Boolean.class.getMethod("parseBoolean", String.class) });
    }

    private void fillOther() throws NoSuchMethodException {
        Class<?> f = Field.class;

        serializable.put(char.class, new Method[] { f.getMethod("getChar", Object.class),
                f.getMethod("setChar", Object.class, char.class) });
        serializable.put(String.class, new Method[] { f.getMethod("get", Object.class),
                f.getMethod("set", Object.class, Object.class) });
    }

}
