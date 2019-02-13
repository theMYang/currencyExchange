package com.wbo.currencyExchange.redis;

public class AssetKey extends BasePrefix {

	public static final int ASSET_EXPIRE = 3600*24*3;
	
	private  AssetKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	
	public static final AssetKey ASSET = new AssetKey(ASSET_EXPIRE, "asset");
	public static final AssetKey FREEZE_ASSET = new AssetKey(ASSET_EXPIRE, "freezeAsset");
}
