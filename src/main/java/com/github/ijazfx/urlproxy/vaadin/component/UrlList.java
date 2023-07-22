package com.github.ijazfx.urlproxy.vaadin.component;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;

import com.github.ijazfx.urlproxy.model.Url;
import com.github.ijazfx.urlproxy.repo.UrlRepository;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.util.storage.FileStorage;
import io.graphenee.util.storage.FileStorage.FileMetaData;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxAbstractEntityList;
import io.graphenee.vaadin.flow.component.GxNotification;

@SpringComponent
@Scope("prototype")
public class UrlList extends GxAbstractEntityList<Url> {

	private static final long serialVersionUID = 1L;

	@Autowired
	UrlRepository repo;

	@Autowired
	UrlForm form;

	@Autowired
	FileStorage storage;

	public UrlList() {
		super(Url.class);
	}

	@Override
	protected Stream<Url> getData() {
		return repo.findAll(Sort.by("title")).stream();
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "title", "alias", "url", "hasCertificate", "isActive" };
	}

	@Override
	protected GxAbstractEntityForm<Url> getEntityForm(Url entity) {
		return form;
	}

	@Override
	protected void onSave(Url entity) {
		String filePath = entity.getFilePath();
		if (filePath != null) {
			File file = new File(filePath);
			if (file.exists()) {
				try {
					Future<FileMetaData> future = storage.save("certificates", filePath);
					FileMetaData fileMetaData = future.get();
					entity.setFilePath(fileMetaData.getResourcePath());
					repo.save(entity);
				} catch (Exception e) {
					e.printStackTrace();
					GxNotification.error(e.getMessage());
				}
			} else {
				repo.save(entity);
			}
		} else {
			repo.save(entity);
		}
	}

	@Override
	protected void onDelete(Collection<Url> entities) {
		repo.delete(entities);
	}

	@Override
	protected void decorateColumn(String propertyName, Column<Url> column) {
		column.setAutoWidth(true);
	}

}
