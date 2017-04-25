package com.qcwy.chaowei.myapplication.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KouKi on 2017/1/11.
 */

public class Cart {
    private List<CartItem> cartItems;

    public Cart() {
        cartItems = new ArrayList<>();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    //添加零件
    public void addPart(Part part) {
        for (CartItem cartItem : cartItems) {
            //有相同的，数量加一
            if (part.equals(cartItem.getPart())) {
                cartItem.setCount(cartItem.getCount() + 1);
                return;
            }
        }
        //没有相同的，添加
        CartItem item = new CartItem();
        item.setCount(1);
        item.setPart(part);
        cartItems.add(item);
    }

    //减少零件
    public void subtractPart(Part part) {
        for (CartItem cartItem : cartItems) {
            //有相同的，数量减一
            if (part.equals(cartItem.getPart())) {
                int count = cartItem.getCount();
                //如果数量为一直接移除
                if (count == 1) {
                    cartItems.remove(cartItem);
                    return;
                }
                cartItem.setCount(count - 1);
            }
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem cartItem = cartItems.get(i);
            Part part = cartItem.getPart();
            sb.append(part.getName());
            sb.append("-");
            sb.append(part.getClassify());
            sb.append("-");
            sb.append(part.getName());
            sb.append("-");
            sb.append("数量:");
            sb.append(cartItem.getCount());
            sb.append("\n");
        }
        return sb.toString();
    }
}
