package com.vaiyee.hongmusic.Utils;

import java.util.List;
import com.vaiyee.hongmusic.bean.Music;

public interface OnLoadSearchFinishListener {
	void onLoadSucess(List<Music> musicList);

	void onLoadFiler();
}
