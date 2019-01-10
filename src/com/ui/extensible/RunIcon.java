package com.ui.extensible;

import javax.swing.Icon;

/**
 * 需要实现动态刷亲图片的实现该接口
 * @author Administrator
 *
 */
public interface RunIcon {
	public void refreIcon(Icon ico);
	public void endIcon();

}
