package com.liao.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.liao.model.Discussions;
import com.liao.service.DiscussionsService;

@Controller
public class DiscussionsController {

	@Autowired
	private DiscussionsService dService;
	
	
	//顯示圖片
	@GetMapping("/discussionsdownloadImage/{articleId}")
	public ResponseEntity<byte[]> downloadImage(@PathVariable Integer articleId){
		Discussions discussion = dService.findById(articleId);
		byte[] photoFile = discussion.getPhotoFile();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		                                // 檔案, header, HttpStatus
		return new ResponseEntity<byte[]>(photoFile, headers, HttpStatus.OK);
	}
	
	@GetMapping("/goBackToMain")
	public String goBackToMain() {
		return "liao/main";
	}
	
	@GetMapping("/discussions")
	public String goHome() {
		return "liao/DiscussionsMain";
	}
	
	
//	@GetMapping("/blog-list")
//	public String go() {
//		return "liao/blog-list";
//	}
	
	@GetMapping("/goBackToHome")
	public String goBackToHome() {
		return "liao/home";
	}
	

	
	@GetMapping("/discussions/insertpage")
	public String insertpage() {
		return "liao/DiscussionInsert";
	}
	
	@PostMapping("/discussions/insertDiscussion")
	public String insertDiscussion(@RequestParam("memberId") Integer memberId,
									@RequestParam("eventId") Integer eventId,
									@RequestParam("userName") String userName,
									@RequestParam("gameId") Integer gameId,
									@RequestParam("gameName") String gameName,
									@RequestParam("title") String title,
									@RequestParam("dcontent") String dcontent,
									@RequestParam("lastReplyTime") String lastReplyTime,
									@RequestParam("d_views") Integer d_views,
									@RequestParam("dcreated_at") String dcreated_at,
									@RequestParam("dlikes") Integer dlikes,
									@RequestParam("photoFile") MultipartFile photoFile,
									Model model) throws IOException {
		Discussions discussions = new Discussions();
		discussions.setMemberId(memberId);
		discussions.setEventId(eventId);
		discussions.setUserName(userName);
		discussions.setGameId(gameId);
		discussions.setGameName(gameName);
		discussions.setTitle(title);
		discussions.setDcontent(dcontent);
		discussions.setLastReplyTime(lastReplyTime);
		discussions.setD_views(d_views);
		discussions.setDcreated_at(dcreated_at);
		discussions.setDlikes(dlikes);
		discussions.setPhotoFile(photoFile.getBytes());
		
		dService.insert(discussions);
		
		return "redirect:/discussions/getAllDiscussions";
	}
	
	@GetMapping("/discussions/getAllDiscussions")
	  public String getAllDiscussions(Model model) throws SQLException {
	          List<Discussions> discussions = dService.findAll();
	          model.addAttribute("discussions", discussions);
	          return "liao/GetAllDiscussion";
	  }
	
	@GetMapping("/getAllDiscussions")
	  public String getAllDiscussion(Model model) throws SQLException {
	          List<Discussions> discussions = dService.findAll();
	          model.addAttribute("discussions", discussions);
	          return "liao/GetAllDiscussion";
	  }
	
	
//	@GetMapping("/discussions/getAllDiscussions")
//	@ResponseBody
//	public  List<Discussions> getAllDiscussions(Model model) throws SQLException {
//		List<Discussions> discussions = dService.findAll();
//		model.addAttribute("discussions", discussions);
//		return discussions;
//	}
	
	@GetMapping("/forum/getfront")
	  public String getfront(Model model) throws SQLException {
	          List<Discussions> discussions = dService.findAll();
	          model.addAttribute("discussions", discussions);
	          return "liao/blog-list";
	  }
	
//	@GetMapping("/forum/DiabloIV")
//	public String goDiabloIV() {
//		return "liao/DiabloIV";
//	}
	
	@GetMapping("/gameforum")
	public String goDiabloIV(Model model) throws SQLException {
		List<Discussions> discussions = dService.findAll();
        model.addAttribute("discussions", discussions);
		return "liao/DiabloIV";
	}
	
	
//	
//	@GetMapping("/discussions/getfront1")
//	@ResponseBody
//	public List<Discussions> getfront1(Model model) throws SQLException {
//		List<Discussions> discussions = dService.findAll();
//		model.addAttribute("discussions", discussions);
//		return discussions;
//	}
	
	
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
	
	@GetMapping("/discussions/update")
	public String updatePage(@RequestParam Integer articleId,Model model) {
		Discussions discussions = dService.findById(articleId);
		model.addAttribute("discussions", discussions);
		return "liao/updateDiscussionData";
	}
	
//	@Transactional
//	@PutMapping("/discussions/update")
//	public String updatePost(@ModelAttribute(name="discussions") Discussions discussions )  throws IOException {
//		
////		byte[] photoFileBytes = null;
////	    if (!photoFile.isEmpty()) {
////	        photoFileBytes = photoFile.getBytes();
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
	@PutMapping("/discussions/update")
	public String updateDiscussion(@RequestParam("articleId") Integer articleId,
            @RequestParam("memberId") Integer memberId,
            @RequestParam("eventId") Integer eventId,
            @RequestParam("userName") String userName,
            @RequestParam("gameId") Integer gameId,
            @RequestParam("gameName") String gameName,
            @RequestParam("title") String title,
            @RequestParam("dcontent") String dcontent,
            @RequestParam("lastReplyTime") String lastReplyTime,
            @RequestParam("d_views") Integer d_views,
            @RequestParam("dcreated_at") String dcreated_at,
            @RequestParam("dlikes") Integer dlikes,
            @RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
            Model model) throws IOException {
			Discussions discussions = dService.findById(articleId);
			
			discussions.setMemberId(memberId);
			discussions.setEventId(eventId);
			discussions.setUserName(userName);
			discussions.setGameId(gameId);
			discussions.setGameName(gameName);
			discussions.setTitle(title);
			discussions.setDcontent(dcontent);
			discussions.setLastReplyTime(lastReplyTime);
			discussions.setD_views(d_views);
			discussions.setDcreated_at(dcreated_at);
			discussions.setDlikes(dlikes);
			
			if (photoFile != null && !photoFile.isEmpty()) {
			discussions.setPhotoFile(photoFile.getBytes());
			}
			
//			dService.update(discussions);
			
			return "redirect:/discussions/getAllDiscussions";
			}
	
	
	@DeleteMapping("/discussions/delete")
	public String deleteDiscussion(@RequestParam("articleId") Integer articleId) {
		dService.deleteById(articleId);
		return "redirect:/discussions/getAllDiscussions";
	}
	
	@GetMapping("/discussions/findByUserName")
	public String findByUserName(@RequestParam("userName") String userName, Model model) {
		List<Discussions> discussions = dService.findDiscussionsByUserName(userName);
		model.addAttribute("discussions", discussions);
//	    model.addAttribute("userName", userName);
		
//		return dService.findDiscussionsByUserName(userName);
//		for (Discussions discussions2 : discussions) {
//			System.out.println(discussions2.getUserName());
//		}
		return "liao/GetDiscussionSelectUserName";
		
	}
	
	

	
	
}
