import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * projectName: srb
 *
 * @author: GOD伟
 * time: 2022/6/20 19:51 周一
 * description:
 */
public class Test1 {
    @Test
    public void b(){
        //服务器密钥
        String serviceKey="atguigu12345";

        //客户端
        String ip="127.0.0.1";

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoi5bCP5by6IiwiYWdlIjozMH0.kGJRvD1X7ifjm7WiHsQjNQQyyULpK3gqd64FO_Dx14U";

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(MD5.encrypt(serviceKey + ip)).parseClaimsJws(token);

        System.out.println(claimsJws);
    }


    @Test
    public void a (){

        // 服务器密钥
        String serviceKey = "atguigu12345";


        // 客户端ip
        String ip = "127.0.0.1";

        // 用户信息
        Map<String,Object> user = new HashMap<>();
        user.put("name","小强");
        user.put("age",30);

        // 生成token
        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")

                .claim("name", (String) user.get("name"))
                .claim("age", (Integer) user.get("age"))

                .signWith(SignatureAlgorithm.HS256, MD5.encrypt(serviceKey + ip))

                .compact();

        System.out.println(token);

    }

}
