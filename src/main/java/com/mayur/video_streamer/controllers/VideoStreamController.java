package com.mayur.video_streamer.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

import com.mayur.video_streamer.services.VideoStreamLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
public class VideoStreamController
{
    private VideoStreamLoader videoStreamLoader;

    public VideoStreamController(VideoStreamLoader videoStreamLoader)
    {
        this.videoStreamLoader = videoStreamLoader;
    }

    @GetMapping(value = "/play/media/v02/{vid_id}")
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> playMediaV02(
            @PathVariable("vid_id")
            String video_id,
            @RequestHeader(value = "Range", required = false)
            String rangeHeader,
            HttpServletRequest req)
    {
        try
        {
            String filePathString = "./media/video/"+video_id+".mp4";
            ResponseEntity<StreamingResponseBody> retVal = videoStreamLoader.loadPartialMediaFile(filePathString, rangeHeader);

            return retVal;
        }
        catch (FileNotFoundException e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
