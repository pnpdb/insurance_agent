package com.gecq.insurance.agent.service.utils;

import com.gecq.insurance.agent.service.models.ModelBean;
import com.gecq.insurance.agent.service.models.State;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Created by gecha on 2016/1/20.
 */
@Component("imageUtils")
public class ImageUtils {

    @Value("#{webConfig['image.server']}")
    private String imageServer;

    @Value("#{webConfig['image.path']}")
    private String imagePath;

    public String getImageServer() {
        return imageServer;
    }

    public void setImageServer(String imageServer) {
        this.imageServer = imageServer;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    private String getFileName(){
        return DateFormatUtils.format(Calendar.getInstance(),"/yyyy/MM/dd/");
    }

    public String uploadImage(MultipartFile imageFile){
        if(imageFile!=null&&!imageFile.isEmpty()){
            try {
                String fileName = getFileName()+UUID.randomUUID().toString()+"."+FilenameUtils.getExtension(imageFile.getOriginalFilename());
                File file = new File(imagePath+ fileName);
                if(!file.getParentFile().exists()){
                    if(!file.getParentFile().mkdirs()){
                        return null;
                    }
                }
                FileCopyUtils.copy(imageFile.getBytes(), new FileOutputStream(file));
                return fileName;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<ModelBean> uploadImages(MultipartFile[] files, int userLevel){
        if(files==null||files.length==0){
            return null;
        }
        State state = State.enable;
        if(userLevel==3){
            state = State.waitAudit;
        }
        List<ModelBean> data = new ArrayList<ModelBean>();
        for(MultipartFile file:files){
            String path = uploadImage(file);
            if(StringUtils.isNotBlank(path)){
                ModelBean modelBean = new ModelBean();
                modelBean.set("url",path).set("state",state.name()).set("id",UUID.randomUUID().toString().replace("-","")).set("type","image");
                data.add(modelBean);
            }
        }
        return data;
    }

    public int delImages(List<ModelBean> images){
        if(images==null||images.size()<1){
            return 0;
        }
        int s=0;
        for(ModelBean image:images){
            if(delImage(image.getString("url"))){
                s++;
            }
        }
        return s;
    }

    public boolean delImage(String url){
        if(StringUtils.isBlank(url)){
            return false;
        }
        String path = imagePath+"/"+url;
        return new File(path).delete();
    }
}
