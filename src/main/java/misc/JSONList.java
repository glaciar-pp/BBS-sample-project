package misc;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONList {

	public static void main(String[] args) {
		List<String> list1 = new ArrayList<>();
		list1.add("cat.jpg");
		
		List<String> list2 = new ArrayList<>();
		list2.add("cat.jpg");
		list2.add("dog.jpg");
		list2.add("cow.jpg");
		
		String str1 = stringify(list1);
		String str2 = stringify(list2);
		
		System.out.println(str2);
		List<String> result = parse(str2);
		result.forEach(x -> System.out.println(x));
	}
	
	public static String stringify(List<String> list) {
		JSONObject obj = new JSONObject();
		obj.put("list", list);
		return obj.toString();
	}
	
	public static List<String> parse(String jsonStr) {
		JSONParser parser = new JSONParser();
		List<String> list = null;
		try {
			JSONObject jsonList = (JSONObject) parser.parse(jsonStr);
			//System.out.println(jsonList); 확인용
			JSONArray jsonArr = (JSONArray) jsonList.get("list");	//[{ }, { }, { }]
			//System.out.println(jsonArr); 확인용
			list = (List<String>) jsonArr;
			//list.forEach(x -> System.out.println(x)); 확인용
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list;

	}
}
