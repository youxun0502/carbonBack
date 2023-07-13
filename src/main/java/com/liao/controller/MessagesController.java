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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.li.service.BonusPointService;
import com.liao.model.Discussions;
import com.liao.model.Messages;
import com.liao.service.MessagesService;
import com.liao.service.DiscussionsService;

@Controller
public class MessagesController {

	@Autowired
	private MessagesService mService;
	@Autowired
	private BonusPointService bpService;
	
	private DiscussionsService dService;
	
	//顯示圖片
	@GetMapping("/messagesdownloadImage/{messageId}")
	public ResponseEntity<byte[]> messagesdownloadImage(@PathVariable Integer messageId){
		Messages msg = mService.findById(messageId);
		byte[] mphotoFile = msg.getMphotoFile();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		                                // 檔案, header, HttpStatus
		return new ResponseEntity<byte[]>(mphotoFile, headers, HttpStatus.OK);
	}
	
//	@GetMapping("/goBackToMain")
//	public String goBackToMain() {
//		return "liao/main";
//	}
//	
	@GetMapping("/messages")
	public String goMessagesHome() {
		return "liao/MessagesMain";
	}
	
//	@GetMapping("/goBackToHome")
//	public String goBackToHome() {
//		return "liao/home";
//	}
	
	@GetMapping("/messages/insertpage")
	public String messagesinsertpage() {
		return "liao/MessageInsert";
	}
	
	@ResponseBody
	@PostMapping("/messages/add")
	public Map<String, Object> addMessages(
//									@RequestParam("articleId") Integer articleId,
//									@RequestParam("memberId") Integer memberId,
//									@RequestParam("userName") String userName,
//									@RequestParam("gameId") Integer gameId,
//									@RequestParam("gameName") String gameName,
//									@RequestParam("mcreated_at") Date mcreated_at,
//									@RequestParam("mcreated") @DateTimeFormat(pattern = "yyyy-MM-dd") Date mcreated,
//									@RequestParam("mlikes") String mlikes,
									@RequestParam("mcontent") String mcontent,
									@RequestParam("mtitle") String mtitle,
//									@RequestParam("mphotoFile") MultipartFile mphotoFile,
									Model model) throws IOException {
		Messages msg = new Messages();
		Integer articleId = 111;
		Integer memberId = 222;
		String userName = "liao";
		msg.setArticleId(articleId );
		
		msg.setMemberId(memberId);
		msg.setUserName(userName);
//		msg.setMcreated(mcreated);
		msg.setMlikes("0");
		msg.setMcontent(mcontent);
//		msg.setGameId(gameId);
//		msg.setGameName(gameName);
		msg.setMtitle(mtitle);
//		msg.setMphotoFile(mphotoFile.getBytes());
		
//		bpService.newPointLog("sendmessage", memberId, 10);
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
	@ResponseBody
	@GetMapping("/messages/find")
	  public List<Messages> getAllAjax(Model model) throws SQLException {
	          List<Messages> msgs = mService.findAll();
	          return msgs;
	  }
	
	@ResponseBody
	@GetMapping("/messages/findByMtitle")
	  public List<Messages> getByMtitleAjax(Model modelm,@RequestParam String mtitle) throws SQLException {
	          List<Messages> msgs = mService.findMessagesByTitle(mtitle);
	          System.out.println("msgs"+msgs.size());
	          return msgs;
	  }
	@PostMapping("/messages/insertmessage")
	public String insertMessages(@RequestParam("articleId") Integer articleId,
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
		
		mService.insert(msg);
		
		return "redirect:/messages/getAllMessages";
	}
	
	@GetMapping("/front/insertpage")
	public String insertpageFront(Model model, @RequestParam String mtitle) {
		model.addAttribute("mtitle", mtitle);
		return "liao/MessageInsertFront";
	}
	
	@PostMapping("/front/insertmessage")
	public String insertMessagesFront(@RequestParam("articleId") Integer articleId,
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
		
		mService.insert(msg);
		
		return "redirect:/forum/title/" +mtitle;
	}
	
	
	@GetMapping("/messages/getAllMessages")
	  public String getAllMessages(Model model) throws SQLException {
	          List<Messages> msg = mService.findAll();
	          model.addAttribute("msg", msg);
	          return "liao/GetAllMessage";
	  }
	
//	@GetMapping("/GetDiscussion")
//	public String selectById(@RequestParam("articleId") int articleId, Model model) {
//	    
//	    	Discussions discussion = dService.findById(articleId);
//	        model.addAttribute("discussion", discussion);
//
//	        return "liao/updateDiscussionData";   
//
// 
//	}
	
	@GetMapping("/messages/update")
	public String updateMessagesPage(@RequestParam Integer messageId,Model model) {
		Messages msg = mService.findById(messageId);
		model.addAttribute("msg", msg);
		return "liao/updateMessageData";
	}
	
//	@Transactional
//	@PutMapping("/discussions/update")
//	public String updatePost(@ModelAttribute(name="discussions") Discussions discussions )  throws IOException {
//		
////		byte[] photoFileBytes = null;
////	    if (!photoFile.isEmpty()) {
////	   )     photoFileBytes = photoFile.getBytes();
////	    }
//		 MultipartFile photoFile = discussions.getPhotoFile();
//		    byte[] photoFileBytes = null;
//		    
//		    if (photoFile != null && !photoFile.isEmpty()) {
//		        photoFileBytes = photoFile.getBytes();
//		    }
//		
//		dService.updateDiscussionsById(discussions.getArticleId(), discussions.getMemberId(),
//				discussions.getEventId(),
//				discussions.getUserName(), 
//				discussions.getGameId(),
//				discussions.getGameName(), 
//				discussions.getTitle(),
//				discussions.getDcontent(),
//				discussions.getLastReplyTime(),
//				discussions.getD_views(),
//				discussions.getDcreated_at(),
//				discussions.getDlikes(),
//				discussions.getPhotoFile());
//		return "redirect:/discussions/getAllDiscussions";
//	}
//	
	@Transactional
	@PutMapping("/messages/update")
	public String updateMessages(@RequestParam("messageId") Integer messageId,
            @RequestParam("articleId") Integer articleId,
            @RequestParam("memberId") Integer memberId,
            @RequestParam("userName") String userName,
            @RequestParam("mcreated") @DateTimeFormat(pattern = "yyyy-MM-dd") Date mcreated,
            @RequestParam("mlikes") String mlikes,
            @RequestParam("mcontent") String mcontent,
            @RequestParam("gameId") Integer gameId,
            @RequestParam("gameName") String gameName,
            @RequestParam("mtitle") String mtitle,
            @RequestParam(value = "mphotoFile", required = false) MultipartFile mphotoFile,
            Model model) throws IOException {
			Messages msg = mService.findById(messageId);
			
			msg.setArticleId(articleId);
			msg.setMemberId(memberId);
			msg.setUserName(userName);
			msg.setMcreated(mcreated);
			msg.setMlikes(mlikes);
			msg.setGameId(gameId);
			msg.setGameName(gameName);
			msg.setMtitle(mtitle);
			msg.setMcontent(mcontent);
			
			if (mphotoFile != null && !mphotoFile.isEmpty()) {
			msg.setMphotoFile(mphotoFile.getBytes());
			}
			
//			dService.update(discussions);
			
			return "redirect:/messages/getAllMessages";
			}
	
	
	@DeleteMapping("/messages/delete")
	public String deleteMessages(@RequestParam("messageId") Integer messageId) {
		mService.deleteById(messageId);
		return "redirect:/messages/getAllMessages";
	}
	
	@GetMapping("/messages/findByUserName")
	public String findMessageByUserName(@RequestParam("userName") String userName, Model model) {
		List<Messages> msg = mService.findMessagesByUserName(userName);
		model.addAttribute("msg", msg);
//	    model.addAttribute("userName", userName);
		
//		return dService.findDiscussionsByUserName(userName);
//		for (Discussions discussions2 : discussions) {
//			System.out.println(discussions2.getUserName());
//		}
		return "liao/GetMessageSelectUserName";
		
	}
	
//	@GetMapping("/forum/title/{title}")
//	  public String getSampleTitle(Model model , @PathVariable String title) throws SQLException {
//	          List<Messages> msg = mService.findMessagesByTitle(mtitle);
//	          model.addAttribute("msg", msg);
//	          return "liao/SampleTitle";
//		
//
//	}
	
//	@GetMapping("/forum/{gameName}")
//	public String goGameDiscussion(Model model , @PathVariable String gameName) throws SQLException {
//		List<Messages> msg = mService.findMessagesByGameName(gameName);
//        model.addAttribute("msg", msg);
//		return "liao/GameDiscussion";
//	}
	
	
//	@ResponseBody
//	@PostMapping("/messages/api/post")
//	public Page<Messages> postMessageApi(@RequestBody Messages msg){
//		
//		if (msg.getArticleId() == null) {
////		     處理 articleId 為空值的情況
////		     可以拋出異常、設定默認值或進行其他處理
////		     例如，若 articleId 不應為空，可以拋出異常提示用戶
//		    throw new IllegalArgumentException("Article ID cannot be null");
//		}
//		
//		mService.insert(msg);
//		
//		Page<Messages> page = mService.findByPage(1);
//		
//		return page;
//	}
	
//	@ResponseBody
//	@GetMapping("/forum/api/page")
//	public Page<Messages> showMessagesApi(@RequestParam(name="p",defaultValue = "1") Integer pageNumber){
//		Page<Messages> page = mService.findByPage(pageNumber);
//		return page;
//	}
	
//	@GetMapping("/messages/page")
//	public String showMessages(@RequestParam(name="p", defaultValue = "1") Integer pageNumber, Model model) {
//		Page<Messages> page = mService.findByPage(pageNumber);
//		
//		model.addAttribute("page", page);
//		
//		return "liao/SampleTitle";
//	}
	
	
//	@PostMapping("/messages/like/{messageId}")
//	public ResponseEntity<Integer> likeMessage(@PathVariable("messageId") Integer messageId) {
//	    // 根據訊息 ID 執行按讚邏輯
//	    // 假設您的 MessagesService 類處理按讚邏輯
//	    int likeCount = mService.likeMessage(messageId);
//	    
//	    // 返回按讚後的數量
//	    return ResponseEntity.ok(likeCount);
//	}
//	
	
	@PostMapping("/messages/like")
	public ResponseEntity<Integer> likeMessage2(@RequestParam("messageId") Integer messageId) {
	    // 根據訊息 ID 執行按讚邏輯
	    // 假設您的 MessagesService 類處理按讚邏輯
	    int likeCount = mService.likeMessage(messageId);
	    
	    // 返回按讚後的數量
	    return ResponseEntity.ok(likeCount);
	}


	   
		@ResponseBody
	    @PostMapping("/updateLikeCount")
	    public ResponseEntity<String> updateLikeCount(@RequestParam("messageId") Integer messageId) {
	        // 根據messageId從資料庫中獲取相應的消息
	        Messages message = mService.findById(messageId);
	        if (message == null) {
	            return ResponseEntity.notFound().build();
	        }
	        
	        // 更新mlike的值
	        int currentLikes = Integer.parseInt(message.getMlikes());
	        currentLikes++;
	        message.setMlikes(String.valueOf(currentLikes));
	        mService.insert(message);
	        // 假設有相應的MessageService提供保存消息的方法
	        
	        return ResponseEntity.ok("按讚數量已更新");
	    }
	

	
	
}
