package io.pivio.view.app.overview.list;

import io.pivio.view.app.overview.list.serverresponse.Overview;
import org.springframework.stereotype.Service;

@Service
public class ModelMapper {

    public OverviewModel map(Overview overview) {
        OverviewModel overviewModel = new OverviewModel();
        overviewModel.context = overview.context;
        overviewModel.domain = overview.domain;
        overviewModel.subdomain = overview.subdomain;
        overviewModel.product = overview.product;
        overviewModel.status = overview.status;
        overviewModel.description = overview.description;
        overviewModel.name = overview.name;
        overviewModel.short_name = overview.short_name;
        overviewModel.lastUpdate = overview.lastUpdate;
        overviewModel.lastUpload = overview.lastUpload;
        overviewModel.id = overview.id;
        overviewModel.owner = overview.owner;
        overviewModel.type = overview.type;
        //links
        overviewModel.jira = overview.jira;
        overviewModel.github = overview.github;
        overviewModel.jenkins = overview.jenkins;
        overviewModel.cloudfoundry = overview.cloudfoundry;
        overviewModel.iteraplan = overview.iteraplan;
        return overviewModel;
    }
}
