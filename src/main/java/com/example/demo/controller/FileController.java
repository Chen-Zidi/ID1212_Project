package com.example.demo.controller;
import ch.qos.logback.core.util.FileUtil;
import com.example.demo.model.*;


import io.netty.handler.codec.http.HttpUtil;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FileController {
    String url = "https://localhost:10086/";
    RoomController rc = new RoomController();

    @RequestMapping("/directFilePage")
    public String directFilePage(HttpServletRequest request,HttpSession session,Model model){
        String rid = request.getParameter("directFileRoomId");
        System.out.println(rid);
        if(session.getAttribute("username") == null){
            return "redirect:/login";
        }

        List<UserFile> fl = requestAllFileForRoomAPI(rid);
        //set model for html
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("RoomList",rc.requestAllRoomAPI());
        model.addAttribute("displayRoom", true);
        model.addAttribute("displayRoomId", rid);
        model.addAttribute("FileList", fl);
        return "indexFile";

    }

    @RequestMapping("/requestFileDelete")
    public String requestDeleteFile(HttpServletRequest request,HttpSession session,Model model){
        String fid = request.getParameter("requestDeleteFileId");
        String rid = request.getParameter("requestDeleteFileRoomId");

        requestDeleteFileAPI(fid);

        List<UserFile> fl = requestAllFileForRoomAPI(rid);

        //set model for html
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("RoomList",rc.requestAllRoomAPI());
        model.addAttribute("displayRoom", true);
        model.addAttribute("displayRoomId", rid);
        model.addAttribute("FileList", fl);
        return "indexFile";

    }

    @RequestMapping("backToMsgIndex")
    public String backToMessageIndex(HttpSession session, Model model,HttpServletRequest request){
        String rid = request.getParameter("msgRoomId");
        List<Room> roomList = rc.requestAllRoomAPI();

        if(session.getAttribute("username") == null){
            return "redirect:/login";
        }

        //set model for html
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("RoomList",roomList);
        model.addAttribute("displayRoom", true);
        model.addAttribute("displayRoomId", rid);
        return "index";
    }

    @RequestMapping("/requestFileDownload")
    public String downloadFiles(HttpServletRequest request,Model model, HttpSession session){
        String fid = request.getParameter("requestDownloadFileId");
        String rid = request.getParameter("requestDownloadFileRoomId");
        String fileName = request.getParameter("requestDownloadFileName");

        String result = downloadFileAPI(fid,fileName);

        List<UserFile> fl = requestAllFileForRoomAPI(rid);
        //set model for html
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("RoomList",rc.requestAllRoomAPI());
        model.addAttribute("displayRoom", true);
        model.addAttribute("displayRoomId", rid);
        model.addAttribute("FileList", fl);
        return "indexFile";
    }

    @RequestMapping("/requestFilesUpload")
    public String uploadFiles(@RequestParam("myfiles") MultipartFile[] files,HttpServletRequest request, HttpServletResponse response, HttpSession session,Model model) throws IOException {
        String rid = request.getParameter("fileRoomId");

        if(!rid.equals("-1")){
            uploadFileAPI(files, rid, session.getAttribute("userId").toString());
        }


        List<UserFile> fl = requestAllFileForRoomAPI(rid);
        //set model for html
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("RoomList",rc.requestAllRoomAPI());
        model.addAttribute("displayRoom", true);
        model.addAttribute("displayRoomId", rid);
        model.addAttribute("FileList", fl);

        return "indexFile";
    }


    public String downloadFileAPI(String fileId, String fileName){


        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<byte[]> response = restTemplate.exchange(url+"file/download?file_id={fid}", HttpMethod.GET, entity, byte[].class, fileId);

            //check dir exists or not
            String path = System.getProperty("user.home")+"\\ProjectDownloadFiles\\";
            File dir = new File(path);
            if(!dir.exists()){
                dir.mkdirs();
            }

            Files.write(Paths.get(System.getProperty("user.home")+"\\ProjectDownloadFiles\\"+fileName), response.getBody());
        }catch (Exception e){
            e.printStackTrace();
        }


        return "ok";
    }

    public String uploadFileAPI(MultipartFile[] files, String roomId, String userId) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        String path = System.getProperty("user.home")+"\\TempFiles\\";
        String fileName = files[0].getOriginalFilename();
        File newFile = new File(path + fileName);
        if(!newFile.getParentFile().exists()){
            newFile.getParentFile().mkdirs();
        }
        files[0].transferTo(newFile);
        FileSystemResource resource = new FileSystemResource(newFile);

//        FileSystemResource[] resources = new FileSystemResource[files.length];
//        for(int i =0; i<files.length;i++){
//            String fileName = files[i].getOriginalFilename();
//            File newFile = new File(path + fileName);
//
//            if(!newFile.getParentFile().exists()){
//                newFile.getParentFile().mkdirs();
//            }
//
//            files[i].transferTo(newFile);
//            resources[i] = new FileSystemResource(newFile);
//
//        }


        //System.out.println(resource);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);

        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        //param.add("files", files);
        param.add("files", resource);
        param.add("room_id", Long.valueOf(roomId));
        param.add("user_id", Long.valueOf(userId));

        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<MultiValueMap<String, Object>>(param, headers);

        String result = restTemplate.postForObject(url+ "file/upload", request, String.class);
        System.out.println("file upload result: " + result);


        return result;
    }


    //call backend api to have all files for the room
    public List<UserFile> requestAllFileForRoomAPI(String roomId)  {
        RestTemplate restTemplate = new RestTemplate();

        Long rid = Long.parseLong(roomId);

        ResponseEntity<List<UserFile>> response = restTemplate.exchange(url+"file/room_files?room_id={id}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserFile>>() {},
                rid
        );

        //ResponseEntity<List<UserFile>> response = restTemplate.exchange("https://localhost:10086/file/room_files", HttpMethod.GET, null, new ParameterizedTypeReference<List<UserFile>>() {}, paramMap);
        List<UserFile> fileList = response.getBody();

        //System.out.println("file list length:" + fileList.size());

        return fileList;

    }

//    //call backend api to have all files for the room
    public String requestDeleteFileAPI(String fileId)  {
        RestTemplate restTemplate = new RestTemplate();

        Long id = Long.valueOf(fileId);

        restTemplate.delete(url+"file/delete?file_id={id}", id);

        return "ok";

    }

}
