package cn.porkchop.ebuy.controller;

import cn.porkchop.ebuy.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
/**
 * 图片的controller
 * @date 2017/12/29 19:43
 * @author porkchop
 */
@Controller
public class PictureController {
    /**
     * 注入图片服务器的地址和端口
     * @date 2017/12/29 19:43
     * @author porkchop
     */
    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;

    /**
     * 图片上传
     * @return
     *      正确返回{error: 0, url: "http://192.168.25.133/group1/M00/00/00/wKgZhVpGKr2AU9UdAAidi-rwBvo668.jpg"}<br>
     *      错误返回{error: 1, message: "图片上传失败"}
     *
     * @date 2017/12/29 19:44
     * @author porkchop
     */
    @RequestMapping("/pic/upload")
    @ResponseBody
    public Map<String, Object> pictureUpload(MultipartFile uploadFile) {
        String originalFilename = uploadFile.getOriginalFilename();
        String extName = originalFilename.substring(originalFilename.indexOf(".") + 1);
        try {
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
            String path = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
            String url = IMAGE_SERVER_URL+path;
            HashMap<String, Object> map = new HashMap<>();
            map.put("error",0);
            map.put("url",url);
            return map;
        } catch (Exception e) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("error",1);
            map.put("message","图片上传失败");
            e.printStackTrace();
            return map;
        }

    }

}
