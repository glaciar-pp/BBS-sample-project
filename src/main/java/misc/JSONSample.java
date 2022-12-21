package misc;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONSample {

	public static void main(String[] args) throws Exception {
		// 내부 Object --> JSON String (= stringify)
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();
		for (int i = 1; i <= 3; i++) {
			JSONObject data = new JSONObject();
			data.put("이름", "인간_" + i);  // String 넣기
			data.put("나이", 20 + i);		// int 넣기
			data.put("주소", "서울 중앙로" + i + "길");	// String 넣기
			arr.add(data);
		}
		
		obj.put("사람들", arr);	
		System.out.println(obj);
		/*{"사람들":[{"이름":"인간_1","주소":"서울 중앙로1길","나이":21},
		{"이름":"인간_2","주소":"서울 중앙로2길","나이":22},
		{"이름":"인간_3","주소":"서울 중앙로3길","나이":23}]} 출력됨*/
		
		// JSON String --> 내부 Object 
		JSONParser parser = new JSONParser();
		JSONObject persons = (JSONObject) parser.parse(obj.toString());
		JSONArray person_arr = (JSONArray) persons.get("사람들");	//[{ }, { }, { }]
		
		JSONObject person = (JSONObject) person_arr.get(0); 
		// 내용 중 0번인 {"이름":"인간_1","주소":"서울 중앙로1길","나이":21} 를 가져옴
		String name = (String) person.get("이름");
		System.out.println(name);	// 위 Object에서 인간_1 를 가져옴
		
		person = (JSONObject) person_arr.get(1); 
		name = (String) person.get("이름");
		System.out.println(name);	//인간_2 를 가져옴
	}

}

//해당 파일의 과정처럼, Java에서 JSON을 사용하는게 생각보다 만만하지는 않음.