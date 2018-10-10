package com.jeeplus.modules.tools;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.Base64;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.FileUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.weixin.entity.WxAccount;
import com.jeeplus.modules.weixin.service.WxAccountService;
import com.jeeplus.wxapi.process.MediaType;
import com.jeeplus.wxapi.process.WxApi;
import com.jeeplus.wxapi.process.WxApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.jeeplus.wxapi.process.WxApi.*;

@Controller
@RequestMapping(value = "/attach/")
public class AttchController {
    @Autowired
    private WxAccountService wxAccountService;

    @ResponseBody
    @RequestMapping({"upfile"})
    public void upfile(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
            throws Exception {
        String json = null;
        response.setContentType("text/html;charset=UTF-8");
        Map<String, Object> result = new HashMap();
        try {
            String attUrl = null;
            for (Iterator it = multipartRequest.getFileNames(); it.hasNext(); ) {
                String key = (String) it.next();
                MultipartFile imgFile = multipartRequest.getFile(key);
                System.out.print(imgFile.getSize());
                if (imgFile.getOriginalFilename().length() > 0) {
                    String originName = imgFile.getOriginalFilename();
                    // 取得文件后缀
                    String fileExt = originName.substring(originName.lastIndexOf(".") + 1);
                    // 文件名
                    originName = System.currentTimeMillis() + "." + fileExt;
                    String realPath = Global.USERFILES_BASE_URL + "upload/" +
                            DateUtils.getYear() + "/" + DateUtils.getMonth() + "/" + DateUtils.getDay() + "/";
                    FileUtils.createDirectory(Global.getUserfilesBaseDir() + realPath);
                    File file = new File(Global.getUserfilesBaseDir() + realPath + originName);
                    imgFile.transferTo(file);
                    attUrl = realPath + originName;
                    result.put("filesPath", file);
                }
            }
            result.put("success", true);
            result.put("msg", "上传成功");
            result.put("code", "-1");
            result.put("AttUrl", attUrl);
            json = JSONUtil.writeObject2Json(result);
            response.getWriter().print(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping({"upfile1"})
    public void upfile1(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
            throws Exception {
        String json = null;
        response.setContentType("text/html;charset=UTF-8");
        Map<String, Object> result = new HashMap();
        try {
            String attUrl = null;
            for (Iterator it = multipartRequest.getFileNames(); it.hasNext(); ) {
                String key = (String) it.next();
                MultipartFile imgFile = multipartRequest.getFile(key);
                System.out.print(imgFile.getSize());
                if (imgFile.getOriginalFilename().length() > 0) {
                    String originName = imgFile.getOriginalFilename();
                    // 取得文件后缀
                    String fileExt = originName.substring(originName.lastIndexOf(".") + 1);
                    // 文件名
                    originName = System.currentTimeMillis() + "." + fileExt;

                    String realPath = Global.USERFILES_BASE_URL + "upload/" +
                            DateUtils.getYear() + "/" + DateUtils.getMonth() + "/" + DateUtils.getDay() + "/";
                    FileUtils.createDirectory(Global.getUserfilesBaseDir() + realPath);
                    File file = new File(Global.getUserfilesBaseDir() + realPath + originName);
                    imgFile.transferTo(file);
                    attUrl = realPath + originName;
                }
            }
            result.put("status", "1");
            result.put("url", attUrl);
            json = JSONUtil.writeObject2Json(result);
            response.getWriter().print(json);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "0");
            result.put("url", null);
        }
    }

    @ResponseBody
    @RequestMapping({"base64Upload"})
    public void uploadAvatar(String fileBase64, HttpServletResponse response) {
        if (StringUtils.isEmpty(fileBase64)) {
            throw new NullPointerException("fileBase64 不能为null");
        } else {
            String json = null;
            File tempImg = null;
            Map<String, Object> result = new HashMap();
            try {
                fileBase64 = fileBase64.replaceFirst("data:image/png;base64,", "");
                fileBase64 = fileBase64.replaceFirst("data:image/jpeg;base64,", "");
                String realPath = Global.USERFILES_BASE_URL + "upload/" +
                        DateUtils.getYear() + "/" + DateUtils.getMonth() + "/" + DateUtils.getDay() + "/";

                realPath += System.currentTimeMillis() + ".png";
                String tempPath = Global.getUserfilesBaseDir() + realPath;

                byte[] byteimg = Base64.decodeFast(fileBase64.trim());
                for (int i = 0; i < byteimg.length; ++i) {
                    if (byteimg[i] < 0) {// 调整异常数据
                        byteimg[i] += 256;
                    }
                }
                FileOutputStream fos = new FileOutputStream(tempPath);
                fos.write(byteimg);
                fos.flush();
                fos.close();
                response.getWriter().print(realPath);
            } catch (Exception e) {
            } finally {
                if (tempImg != null) {
                    tempImg.delete();
                }
            }
        }
    }

    public double getSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小，如果是文件则直接返回其大小
            if (!file.isFile()) {
                //获取文件大小
                File[] fl = file.listFiles();
                double ss = 0;
                for (File f : fl)
                    ss += getSize(f);
                return ss;
            } else {
                double ss = (double) file.length() / 1024 / 1024;
                System.out.println(file.getName() + " : " + ss + "MB");
                return ss;
            }
        } else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0.0;
        }
    }

    // 保存文件
    @SuppressWarnings("unused")
    private File saveFileFromInputStream(InputStream stream, String path, String filename) throws IOException {
        try {
            File file = new File(path + "/" + filename);

            FileOutputStream fs = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 1024];
            int bytesum = 0;
            int byteread = 0;
            while ((byteread = stream.read(buffer)) != -1) {
                bytesum += byteread;
                fs.write(buffer, 0, byteread);
                fs.flush();
            }

            fs.close();
            stream.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
