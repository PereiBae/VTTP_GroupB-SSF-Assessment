package vttp.batch5.ssf.noticeboard.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.services.NoticeService;

// Use this class to write your request handlers
@Controller
@RequestMapping
public class NoticeController {

    @Autowired
    private NoticeService noticeSvc;

    @GetMapping
    public String getNotice(Model model) {
        model.addAttribute("notice", new Notice()); // Add the NoticeModel to the model
        return "notice"; // Return the template name
    }

    @PostMapping("/notice")
    public String postToNoticeServer(@Valid Notice notice, BindingResult binding, Model model) {

        if (binding.hasErrors()) {
            binding.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            model.addAttribute("notice", notice); // Re-add the NoticeModel with errors
            return "notice";
        } else {

            try {
                // Attempt API authentication
                String message = noticeSvc.postToNoticeServer(notice.getTitle(), notice.getPoster(), notice.getPostDate(), notice.getCategories(), notice.getText());
                model.addAttribute("id", message);
                return "view2";

            } catch (Exception e) {
                e.printStackTrace();
                binding.addError(new ObjectError("noticeFailed", "Notice did not meet criteria to be posted"));
                return "view3";
            }
        }

    }

    @GetMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<HttpStatus> getStatus() {
        if (noticeSvc.getRandomKey()) {
            return new ResponseEntity<>(HttpStatus.valueOf(503));
        }
        return new ResponseEntity<>(HttpStatus.valueOf(200));
    }

}
