package com.example.pwd61.analysis.Detour.tools;


import java.lang.reflect.Field;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**************************************************************************
 * project:FuckJD
 * Email: 
 * file:ConvJSON
 * Created by pwd61 on 2019/4/1 18:35
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class ConvJSON {

    private String SimpleObjectToJson(Object o) {
        StringBuffer sf = null;
        sf = new StringBuffer("{");
        //sf.append(o.getClass().getSimpleName());
        Field field[] = o.getClass().getDeclaredFields();
        String content = "";
        for (Field f : field) {
            try {
                f.setAccessible(true);
                content += "\"" + f.getName() + "\":" + convertNull(f.get(o)) + ",";
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        sf.append(content);
        return sf.toString().substring(0, sf.length() - 1) + "}";
    }

    public Object convertNull(Object o) {
        if (o == null) {
            return "\"\"";
        } else {
            return convertJSON(o);
        }
    }

    @SuppressWarnings("unchecked")
    public Object convertJSON(Object o) {
        if (o == null) {
            return "\"\"";
        } else if (o instanceof Boolean) {
            return "\"" + o.toString() + "\"";
        } else if (o instanceof Character) {
            return "\"" + o.toString() + "\"";
        } else if (o instanceof Short) {
            return "\"" + o.toString() + "\"";
        } else if (o instanceof Integer) {
            return "\"" + o.toString() + "\"";
        } else if (o instanceof Long) {
            return "\"" + o.toString() + "\"";
        } else if (o instanceof Float) {
            return "\"" + o.toString() + "\"";
        } else if (o instanceof Double) {
            return "\"" + o.toString() + "\"";
        } else if (o instanceof Byte) {
            return "\"" + o.toString() + "\"";
        } else if (o instanceof List) {
            return listToJSON((List) o);
        } else if (o instanceof Map) {
            return mapToJSON((Map) o);
        } else if (o.getClass().isArray()) {
            return arrayToJSON(o);
        } else if (o instanceof String) {
            return "\"" + o.toString() + "\"";
        } else if (o instanceof Date) {
            return "\"" + o.toString() + "\"";
        }
        return SimpleObjectToJson(o);
    }

    @SuppressWarnings("unchecked")
    private String listToJSON(List list) {
        StringBuffer
                sf = new StringBuffer("[");
        for (int i = 0; i < list.size(); i++) {
            sf.append(convertJSON(list.get(i)) + ",");
        }
        return sf.toString().substring(0, sf.toString().length() - 1) + "]";
    }

    @SuppressWarnings("unchecked")
    private String mapToJSON(Map map) {
        StringBuffer sf = new StringBuffer("[");
        Iterator ite = map.entrySet().iterator();
        while (ite.hasNext()) {
            Entry o = (Entry) ite.next();
            String key = convertJSON(o.getKey()).toString();
            sf.append("{\"" + key.substring(1, key.length() - 1) + "\":");
            sf.append(convertJSON(o.getValue()) + "},");
        }
        return sf.toString().substring(0, sf.toString().length() - 1) + "]";
    }

    public String arrayToJSON(Object o) {
        StringBuffer sf = new StringBuffer("[");
        Object[] obj = (Object[]) o;
        for (int i = 0; i < obj.length; i++) {
            sf.append(convertJSON(obj[i]) + ",");
        }
        return sf.toString().substring(0, sf.toString().length() - 1) + "]";
    }

    //the sum method convert to json key --> value
    public static String putJSON(String key, Object value) {
        ConvJSON joc = new ConvJSON();
        String jsonString = "{\"" + key + "\":" + joc.convertJSON(value) + "}";
        return jsonString;
    }

}
