package com.wibe.backend.threads;

import java.util.List;

import com.wibe.backend.dto.WibeInfoDTO;
import com.wibe.backend.entities.models.Wibe;
import com.wibe.backend.library.WibeLib;
import com.wibe.backend.repositories.UserRepository;
import com.wibe.backend.repositories.WibeRepository;

public class WibeDTOUpdateThread implements Runnable{
	
	private List<WibeInfoDTO> wibedto;
	private long uid;
	private WibeRepository wibeRepository;
	private UserRepository userRepository;
	private Wibe wibe;
	private boolean addCounts = true;
	private boolean addUserInfo = true;
	private WibeLib wibelib  = WibeLib.getInstance();
	private int pos;
	private boolean onlyViews = false;
	
	public WibeDTOUpdateThread(List<WibeInfoDTO> wibedto, Wibe wibe,
			WibeRepository wibeRepository, UserRepository userRepository, long uid, int pos) {
		// TODO Auto-generated constructor stub
		this.wibedto = wibedto;
		this.wibe = wibe;
		this.wibeRepository = wibeRepository;
		this.userRepository = userRepository;
		this.uid = uid;
		this.pos = pos;
	}
	
	public WibeDTOUpdateThread(List<WibeInfoDTO> wibedto, Wibe wibe,
			WibeRepository wibeRepository, UserRepository userRepository, long uid, int pos,
			boolean addCounts, boolean addUserInfo, boolean onlyViews) {
		this(wibedto,wibe,wibeRepository, userRepository, uid, pos);
		this.addCounts = addCounts;
		this.addUserInfo = addUserInfo;
		this.onlyViews = onlyViews;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		WibeInfoDTO w = new WibeInfoDTO(wibe); 
		if (this.addCounts){
			wibelib.addCounts(w, wibeRepository, wibe.getWibeId(), uid, onlyViews, wibe);
		}
		if (this.addUserInfo){
			wibelib.addUserInfo(w, userRepository, wibe.getUploaderId());
		}
		wibedto.set(pos, w);	
	}

}
