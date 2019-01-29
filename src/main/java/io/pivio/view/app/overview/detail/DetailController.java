package io.pivio.view.app.overview.detail;

import io.pivio.view.PivioServerConnector;
import io.pivio.view.configuration.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class DetailController {

    @Autowired
    ServerConfig serverConfig;

    @Autowired
    DetailService detailService;

    @Autowired
    PivioServerConnector pivioServerConnector;

    @RequestMapping(value = "/app/overview/{_id}")
    public String detail(@PathVariable String _id, Model model) {
        model.addAttribute("config", serverConfig);
        model.addAttribute("pageId", "tabOverview");
        model.addAttribute("pivio", detailService.getDetail(_id));
        return "detail";
    }

    @RequestMapping(value = "/app/overview/{_id}/delete")
    public String delete(@PathVariable String _id, RedirectAttributes redirectAttributes) {
        try {
            if (pivioServerConnector.deleteDocument(_id)) {
                redirectAttributes.addFlashAttribute("pivioMessage", "Document with _id " + _id + " was deleted.");
            } else {
                redirectAttributes.addFlashAttribute("pivioErrorMessage", "Document with _id " + _id + " was NOT deleted.");
            }
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("pivioErrorMessage", "Document with _id " + _id + " was NOT deleted. Could not communicate with document server.");
        }
        return "redirect:/app/overview";
    }
}
