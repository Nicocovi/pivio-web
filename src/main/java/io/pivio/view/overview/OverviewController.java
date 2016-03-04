package io.pivio.view.overview;

import io.pivio.view.configuration.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OverviewController {

  private final Logger log = LoggerFactory.getLogger(OverviewController.class);

  @Autowired
  ServerConfig serverConfig;

  @RequestMapping("/")
  public String main() {
    return "redirect:/app/overview";
  }

  @RequestMapping("/app/overview")
  public String overview(Model model) {
    model.addAttribute("config", serverConfig);
    model.addAttribute("pageId", "tabOverview");
    return "overview";
  }

  @RequestMapping("/app/detail/{id}")
  public String detail(@PathVariable String id, Model model) {
    model.addAttribute("config", serverConfig);
    model.addAttribute("pageId", "tabOverview");
    model.addAttribute("pivioDocumentId", id);
    return "detail";
  }

}
