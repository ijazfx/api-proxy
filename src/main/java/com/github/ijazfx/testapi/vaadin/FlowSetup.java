package com.github.ijazfx.testapi.vaadin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.github.ijazfx.testapi.vaadin.component.UrlView;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import io.graphenee.vaadin.flow.GxMenuItemFactory;
import io.graphenee.vaadin.flow.base.GxAbstractFlowSetup;
import io.graphenee.vaadin.flow.base.GxMenuItem;
import io.graphenee.vaadin.flow.documents.GxDocumentExplorerView;

@Component
@VaadinSessionScope
public class FlowSetup extends GxAbstractFlowSetup {

	@Override
	public String appTitle() {
		return "Test API";
	}

	@Override
	public Image appLogo() {
		//		Image i = new Image();
		//		i.setSrc("frontend/images/graphenee.png");
		//		return i;
		return null;
	}

	@Override
	public String appVersion() {
		return "1.0";
	}

	@Override
	public List<GxMenuItem> menuItems() {
		List<GxMenuItem> items = new ArrayList<>();
		items.add(GxMenuItemFactory.setupMenuItem());
		items.add(GxMenuItem.create("Documents", VaadinIcon.FOLDER_O.create(), GxDocumentExplorerView.class));
		items.add(GxMenuItemFactory.messageTemplateMenuItem());
		items.add(GxMenuItem.create("Manage URLs", VaadinIcon.GLOBE.create(), UrlView.class));

		return items;
	}

	@Override
	public Class<? extends RouterLayout> routerLayout() {
		return MainLayout.class;
	}

}
