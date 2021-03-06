package com.xuya.charge.phone.intf.listener;

import java.util.List;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.xuya.charge.phone.infra.cache.guava.ClientCache;
import com.xuya.charge.phone.infra.cache.guava.PhoneBlackCache;
import com.xuya.charge.phone.infra.cache.guava.WhiteIPCache;
import com.xuya.charge.phone.infra.dao.ClientRepository;
import com.xuya.charge.phone.infra.dao.PhoneBlackRepository;
import com.xuya.charge.phone.infra.dao.WhiteIPRepository;
import com.xuya.charge.phone.infra.dao.entity.Client;
import com.xuya.charge.phone.infra.dao.entity.PhoneBlack;
import com.xuya.charge.phone.infra.dao.entity.WhiteIP;

public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ClientRepository clientRepository = event.getApplicationContext().getBean(ClientRepository.class);
		List<Client> listClient = clientRepository.findAll();
		if (listClient != null) {
			listClient.forEach(client -> {
				ClientCache.put(String.valueOf(client.getId()), client.getSecret());
			});
		}
		WhiteIPRepository whiteIPRepository = event.getApplicationContext().getBean(WhiteIPRepository.class);
		List<WhiteIP> listIP = whiteIPRepository.findAll();
		if (listIP != null) {
			listIP.forEach(ip -> {
				WhiteIPCache.put(String.valueOf(ip.getId()), ip.getIps() == null ? "" : ip.getIps());
			});
		}
		PhoneBlackRepository phoneBlackRepository = event.getApplicationContext().getBean(PhoneBlackRepository.class);
		List<PhoneBlack> listPhoneBlack = phoneBlackRepository.findAll();
		if (listPhoneBlack != null) {
			listPhoneBlack.forEach(phone -> {
				PhoneBlackCache.put(phone.getPhone(), "0");
			});
		}
	}

}
