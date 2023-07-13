package com.liao.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.multipart.MultipartFile;

import com.evan.dto.GameDTO;
import com.evan.service.GameService;
import com.liao.model.Discussions;
import com.liao.model.Messages;
import com.liao.service.DiscussionsService;
import com.liao.service.MessagesService;


@Controller
public class DiscussionsController {

	@Autowired
	private DiscussionsService dService;
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private MessagesService mService;
	
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
	
	@GetMapping("/forum/insertpage")
	public String insertpageForum(Model model, @RequestParam String gameName) {
		model.addAttribute("gameName", gameName);
		return "liao/DiscussionInsertFront";
	}
	
	@PostMapping("/forum/insertDiscussion")
	public String insertDiscussionForum(@RequestParam("memberId") Integer memberId,
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
		
		return "redirect:/forum/" +gameName;
	}
	
	@GetMapping("/forum/insertpageMessage")
	public String insertpageForumMessage() {
		return "liao/DiscussionInsertFrontMessage";
	}
	
	@PostMapping("/forum/insertDiscussionMessage")
	public String insertDiscussionForumMessage(@RequestParam("memberId") Integer memberId,
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
		
		return "redirect:/forum/" +gameName;
	}
	
//	@ResponseBody
//	@GetMapping("/forum/api/page")
//	public Page<Discussions> showMessagesApi(@RequestParam(name="p",defaultValue = "1") Integer pageNumber){
//		Page<Discussions> page = dService.findByPage(pageNumber);
//		return page;
//	}
	
//	@ResponseBody
//	@PostMapping("/messages/api/post")
//	public Page<Discussions> postMessageApi(@RequestBody Discussions discussions){
//		dService.insert(discussions);
//		
//		Page<Discussions> page = dService.findByPage(1);
//		
//		return page;
//	}
	
	
	
	@GetMapping("/discussions/getAllDiscussions")
	  public String getAllDiscussions(Model model) throws SQLException {
	          List<Discussions> discussions = dService.findAll();
	          model.addAttribute("discussions", discussions);
	          return "liao/GetAllDiscussion";
	  }
	
	@GetMapping("/getAllDiscussions")
	  public String getAllDiscussion(Model model) throws SQLException {
	          List<Discussions> discussions = dService.findAll();
	          
	          List<Discussions> uniqueGameNames = new ArrayList<>();
	          List<String> gameName = new ArrayList<>();

	       // 遍歷discussions列表
//	       for (int i = 0; i < discussions.size(); i++) {
//	           String currentGameName = discussions[i].getGameName();
//	           
//	           // 檢查相鄰的元素是否具有相同的gameName
//	           if (i == 0 || !currentGameName.equals(discussions.get(i - 1).getGameName())) {
//	               uniqueGameNames.add(currentGameName);
//	           }
//	       }
	       
	          for (Discussions discussion : discussions) {
	        	  for (String game : gameName) {
					if(discussion.getGameName() != game) {
						uniqueGameNames.add(discussion);
						gameName.add(discussion.getGameName()) ;
					}
				}
			}

	       // 將uniqueGameNames和discussions傳遞到模板中
	       model.addAttribute("uniqueGameNames", uniqueGameNames);
	      
	          
	         // model.addAttribute("discussions", discussions);
	          return "liao/GetAllDiscussion";
	  }
	
//	@GetMapping("/forum/getfront")
//	  public String getfront(Model model) throws SQLException {
//	          List<Discussions> discussions = dService.findAll();
//	          
//	          List<Discussions> uniqueGameNames = new ArrayList<>();
//	          List<String> gameName = new ArrayList<>();
//
//	       // 遍歷discussions列表
////	       for (int i = 0; i < discussions.size(); i++) {
////	           String currentGameName = discussions[i].getGameName();
////	           
////	           // 檢查相鄰的元素是否具有相同的gameName
////	           if (i == 0 || !currentGameName.equals(discussions.get(i - 1).getGameName())) {
////	               uniqueGameNames.add(currentGameName);
////	           }
////	       }
//	       
//	          for (Discussions discussion : discussions) {
//	        	  for (String game : gameName) {
//					if(discussion.getGameName() != game) {
//						uniqueGameNames.add(discussion);
//						gameName.add(discussion.getGameName()) ;
//					}
//				}
//			}
//
//	       // 將uniqueGameNames和discussions傳遞到模板中
//	       model.addAttribute("uniqueGameNames", uniqueGameNames);
//	      
//	          
//	          model.addAttribute("discussions", discussions);
//	       return "liao/blog-list";
//	  }
	
	
	
	
//	@GetMapping("/discussions/getAllDiscussions")
//	@ResponseBody
//	public  List<Discussions> getAllDiscussions(Model model) throws SQLException {
//		List<Discussions> discussions = dService.findAll();
//		model.addAttribute("discussions", discussions);
//		return discussions;
//	}
	
//	@GetMapping("/forum/getfront")
//	  public String getfront(Model model) throws SQLException {
//        List<Discussions> discussions = dService.findAll();
//        
//  
//        
//        List<Discussions> uniqueGameNames = new ArrayList<>();
//        List<String> gameName = new ArrayList<>();
//      List<GameDTO> gameDto = gameService.getAllGameInfo();
//        for (GameDTO gameDTO2 : gameDto) {
//        	gameName.add(gameDTO2.getGameName()) ;
//		}
//
//     
//        for (Discussions discussion : discussions) {
//        	System.out.println(1);
//      	  for (String game : gameName) {
//
//				if(discussion.getGameName().equals(game) ) {
//					System.out.println(discussion.getGameName());
//					uniqueGameNames.add(discussion);
//					gameName.remove(game);
//				      	  if(gameName.size()==0) {
//      		  break;
//      	  }
//				}
//			}
//
//		}
//        System.out.println(uniqueGameNames);
//
//
//     model.addAttribute("uniqueGameNames", uniqueGameNames);
//    
//	          
//	          return "liao/blog-list";
//	  }
	
	@GetMapping("/forum/getfront")
	public String getfront(Model model) throws SQLException {
	    List<Discussions> discussions = dService.findAll();
	    List<Discussions> uniqueGameNames = new ArrayList<>();
	    List<String> gameName = new ArrayList<>();
	    List<GameDTO> gameDto = gameService.getAllGameInfo();
	    Map<String, Integer> photos = new HashMap<>();
	    
	    for (GameDTO gameDTO2 : gameDto) {
	        gameName.add(gameDTO2.getGameName());
	        photos.put(gameDTO2.getGameName(), gameDTO2.getGamePhotoLists().get(0));
	    }

	    Iterator<String> iterator = gameName.iterator();
	    while (iterator.hasNext()) {
	        String game = iterator.next();
	        for (Discussions discussion : discussions) {
	            if (discussion.getGameName().equals(game)) {
	            	
	            	discussion.setPhotoId(photos.get(game));
	                System.out.println(discussion.getGameName());
	                uniqueGameNames.add(discussion);
	                iterator.remove(); // 使用Iterator的remove()方法刪除元素
	                break;
	            }
	        }
	        if (gameName.isEmpty()) {
	            break;
	        }
	    }
	    
	    System.out.println(uniqueGameNames);
	    model.addAttribute("uniqueGameNames", uniqueGameNames);
	    
	    return "/liao/blog-list";
	}

	
	
	@GetMapping("/forum/{gameName}")
	public String goGameDiscussion(Model model , @PathVariable String gameName) throws SQLException {
		List<Discussions> discussions = dService.findDiscussionsByGameName(gameName);
        model.addAttribute("discussions", discussions);
        model.addAttribute("gameName", gameName);
		return "liao/GameDiscussion";
	}
	
//	@GetMapping("/forum/apex")
//	public String goapex(Model model) throws SQLException {
//		List<Discussions> discussions = dService.findAll();
//        model.addAttribute("discussions", discussions);
//		return "liao/apex";
//	}
//	
//	@GetMapping("/forum/maplestory")
//	public String gomaplestory(Model model) throws SQLException {
//		List<Discussions> discussions = dService.findAll();
//        model.addAttribute("discussions", discussions);
//		return "liao/maplestory";
//	}
	
	@GetMapping("/forum/title/{mtitle}")
	  public String getTitle(Model model , @PathVariable String mtitle) throws SQLException {
	          List<Discussions> discussions = dService.findDiscussionsByTitle(mtitle);
	          model.addAttribute("discussions", discussions);
//	          return "liao/SampleTitle";
		   List<Messages> msg = mService.findMessagesByTitle(mtitle);
           model.addAttribute("msg", msg);
           model.addAttribute("mtitle", mtitle);
           return "liao/SampleTitle";
	  }
	
	
//	@GetMapping("/gameforum")
//	public String goDiabloIV(Model model) throws SQLException {
//		List<Discussions> discussions = dService.findAll();
//        model.addAttribute("discussions", discussions);
//		return "liao/DiabloIV";
//	}
	
	
	
	
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
	
//	@GetMapping("/forum/updatefront")
//	public String updatePageFront(@RequestParam Integer articleId,Model model) {
//		Discussions discussions = dService.findById(articleId);
//		model.addAttribute("discussions", discussions);
//		return "liao/updateDiscussionData";
//	}
	
//	@Transactional
//	@PutMapping("/forum/updatefront")
//	public String updateDiscussionFront(@RequestParam("articleId") Integer articleId,
//            @RequestParam("memberId") Integer memberId,
//            @RequestParam("eventId") Integer eventId,
//            @RequestParam("userName") String userName,
//            @RequestParam("gameId") Integer gameId,
//            @RequestParam("gameName") String gameName,
//            @RequestParam("title") String title,
//            @RequestParam("dcontent") String dcontent,
//            @RequestParam("lastReplyTime") String lastReplyTime,
//            @RequestParam("d_views") Integer d_views,
//            @RequestParam("dcreated_at") String dcreated_at,
//            @RequestParam("dlikes") Integer dlikes,
//            @RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
//            Model model) throws IOException {
//			Discussions discussions = dService.findById(articleId);
//			
//			discussions.setMemberId(memberId);
//			discussions.setEventId(eventId);
//			discussions.setUserName(userName);
//			discussions.setGameId(gameId);
//			discussions.setGameName(gameName);
//			discussions.setTitle(title);
//			discussions.setDcontent(dcontent);
//			discussions.setLastReplyTime(lastReplyTime);
//			discussions.setD_views(d_views);
//			discussions.setDcreated_at(dcreated_at);
//			discussions.setDlikes(dlikes);
//			
//			if (photoFile != null && !photoFile.isEmpty()) {
//			discussions.setPhotoFile(photoFile.getBytes());
//			}
//			
////			dService.update(discussions);
//			
//			return "redirect:/discussions/getAllDiscussions";
//			}
	
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
//		return "redirect:/forum/DiabloIV";
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
	
//	@GetMapping("/forum/findByGameName")
//	public String findByGameName(@RequestParam("gameName") String gameName, Model model) {
//		List<Discussions> discussions = dService.findDiscussionsByGameName(gameName);
//		model.addAttribute("discussions", discussions);
////	    model.addAttribute("userName", userName);
////		model.addAttribute("gameName", gameName);
////		return dService.findDiscussionsByUserName(userName);
////		for (Discussions discussions2 : discussions) {
////			System.out.println(discussions2.getUserName());
////		}
//		return "redirect:/forum/" +gameName;
//		
//	}
	
	@GetMapping("/searchByGameName")
	public String searchByGameName(@RequestParam("gameName") String gameName) {
	    return "redirect:/forum/" + gameName;
	}
	
	
	
	

	
	
}
