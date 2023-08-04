package com.liao.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.liao.model.Discussions;
import com.liao.model.Messages;
import com.liao.service.MessagesService;
import com.liao.service.DiscussionsService;

@RestController
@Controller
@RequestMapping("messageajax")
public class MsgAjaxController {

	@Autowired
	private MessagesService mService;
	
	private DiscussionsService dService;
	

	
	@ResponseBody
	@PostMapping("add")
	public Map<String, Object> addMessages(@RequestParam("articleId") Integer articleId,
									@RequestParam("memberId") Integer memberId,
									@RequestParam("userName") String userName,
									@RequestParam("gameId") Integer gameId,
									@RequestParam("gameName") String gameName,
//									@RequestParam("mcreated_at") Date mcreated_at,
									@RequestParam("mcreated") @DateTimeFormat(pattern = "yyyy-MM-dd") Date mcreated,
									@RequestParam("mlikes") String mlikes,
									@RequestParam("mcontent") String mcontent,
									@RequestParam("mtitle") String mtitle,
									@RequestParam("mphotoFile") MultipartFile mphotoFile,
									Model model) throws IOException {
		Messages msg = new Messages();
		msg.setArticleId(articleId);
		msg.setMemberId(memberId);
		msg.setUserName(userName);
		msg.setMcreated(mcreated);
		msg.setMlikes(mlikes);
		msg.setMcontent(mcontent);
		msg.setGameId(gameId);
		msg.setGameName(gameName);
		msg.setMtitle(mtitle);
		msg.setMphotoFile(mphotoFile.getBytes());
		
		Map<String, Object> retMap = new HashMap<>();
		try {
			mService.insert(msg);
			retMap.put("code", 200);
			retMap.put("message", "成功");
			
		} catch (Exception e) {
			retMap.put("code", 400);
			retMap.put("message", e.getMessage());
		}
		
		return retMap;
	}	

	
	
}
