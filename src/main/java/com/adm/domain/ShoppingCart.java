package com.adm.domain;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by Milan_Verheij on 26-08-16.
 *
 * Shopping Cart
 */

@Component
@Scope(
        value= WebApplicationContext.SCOPE_GLOBAL_SESSION,
        proxyMode = ScopedProxyMode.INTERFACES)
public class ShoppingCart implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6306460528417060539L;
	private HashSet<BestelArtikel> winkelwagen = new HashSet<>();

    // Getters & Setters
    public HashSet<BestelArtikel> getWinkelwagen() {
        return winkelwagen;
    }
    public void setWinkelwagen(HashSet<BestelArtikel> winkelwagen) {
        this.winkelwagen = winkelwagen;
    }
}
