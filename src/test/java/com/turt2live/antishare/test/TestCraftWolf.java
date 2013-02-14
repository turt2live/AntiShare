package com.turt2live.antishare.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import com.turt2live.antishare.test.util.CraftWolf;

@RunWith (PowerMockRunner.class)
public class TestCraftWolf {

	@Test
	public void test(){
		CraftWolf wolf = new CraftWolf();
		Method[] methods = CraftWolf.class.getMethods();
		for(Method method : methods){
			// Remove Java methods
			if(method.getName().equalsIgnoreCase("equals") || method.getName().equalsIgnoreCase("toString")
					|| method.getName().equalsIgnoreCase("hashcode") || method.getName().equalsIgnoreCase("getClass")){
				continue;
			}
			try{
				Type[] types = method.getGenericParameterTypes();
				if(types != null){
					method.invoke(wolf, new Object[types.length]);
				}else{
					method.invoke(wolf);
				}
				throw new UnknownError("Call passed: " + method.getName());
			}catch(UnsupportedOperationException e){}catch(IllegalArgumentException e){}catch(IllegalAccessException e){}catch(InvocationTargetException e){}
		}
	}

}
