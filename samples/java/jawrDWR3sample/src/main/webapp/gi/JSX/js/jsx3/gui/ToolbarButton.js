/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.Form","jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.ToolbarButton",jsx3.gui.Block,[jsx3.gui.Form],function(r,d){var Ob=jsx3.gui.Form;var z=jsx3.gui.Interactive;var nc=jsx3.gui.Event;r.TYPENORMAL=0;r.TYPECHECK=1;r.TYPERADIO=2;r.STATEOFF=0;r.STATEON=1;r.IMAGEDOWN=jsx3.resolveURI("jsx:///images/tbb/down.gif");r.IMAGEON=jsx3.resolveURI("jsx:///images/tbb/on.gif");r.IMAGEOVER=jsx3.resolveURI("jsx:///images/tbb/over.gif");r.DEFAULTIMAGE=jsx3.resolveURI("jsx:///images/tbb/default.gif");r.BORDERCOLOR="#9B9BB7";jsx3.html.loadImages(r.IMAGEDOWN,r.IMAGEON,r.IMAGEOVER,r.DEFAULTIMAGE);d.init=function(l,c,i){this.jsxsuper(l,null,null,null,null);if(c!=null)this.setType(c);if(i!=null)this.setTip(i);};d.getDisabledImage=function(){return this.jsxdisabledimage!=null&&this.jsxdisabledimage.trim()!=""?this.jsxdisabledimage:this.getImage();};d.setDisabledImage=function(s){this.jsxdisabledimage=s;return this;};d.doValidate=function(){var bb=this.getType==r.TYPENORMAL||this.getRequired()==Ob.OPTIONAL||this.getState()==r.STATEON;return this.setValidationState(bb?Ob.STATEVALID:Ob.STATEINVALID).getValidationState();};d.getImage=function(){return this.jsximage!=null&&this.jsximage.trim()!=""?this.jsximage:null;};d.setImage=function(l){this.jsximage=l;return this;};d.getType=function(){return this.jsxtype==null?r.TYPENORMAL:this.jsxtype;};d.setType=function(e){this.jsxtype=e;return this;};d.DY=function(o,q){if(o.spaceKey()||o.enterKey()){this.wd(o,q);o.cancelAll();}};d.getMaskProperties=function(){return jsx3.gui.Block.MASK_NO_EDIT;};d.mL=function(s,m){if(!s.leftButton())return;m.style.backgroundImage="url("+r.IMAGEDOWN+")";m.childNodes[3].style.backgroundColor=r.BORDERCOLOR;if(this.getEvent(z.MENU)!=null)m.childNodes[2].style.backgroundImage="url("+r.IMAGEDOWNMENU+")";};d._4=function(p,n){if(p.leftButton()){n.style.backgroundImage="";n.childNodes[3].style.backgroundColor="";}else{if(p.rightButton()){this.jsxsupermix(p,n);}}};d.CL=function(s,m){if(this.getState()==r.STATEOFF){m.style.backgroundImage="url("+r.IMAGEOVER+")";m.childNodes[3].style.backgroundColor="#808080";}};d.u2=function(j,c){if(this.getState()==r.STATEOFF){c.style.backgroundImage="";c.childNodes[3].style.backgroundColor="";}};d.doExecute=function(o){if(o==null)o=true;this.wd(o,this.getRendered(o instanceof nc?o:null));};d.doClick=function(){this.wd(true,this.getRendered());};d.wd=function(j,c){var R=this.doEvent(z.EXECUTE,{objEVENT:j instanceof nc?j:null});if(R!==false){if(this.getType()==r.TYPERADIO){this.Vf(r.STATEON,j,c);}else{if(this.getType()==r.TYPECHECK){this.Vf(this.getState()==r.STATEON?r.STATEOFF:r.STATEON,j,c);}}}};d.getGroupName=function(){return this.jsxgroupname!=null&&this.getType()==r.TYPERADIO?this.jsxgroupname:null;};d.setGroupName=function(j){if(this.getType()==r.TYPERADIO)this.jsxgroupname=j;return this;};d.getDivider=function(){return this.jsxdivider!=null?this.jsxdivider:0;};d.setDivider=function(p,j){this.jsxdivider=p;if(j)this.recalcBox(["border","padding"]);else this.C5();return this;};d.getState=function(){return this.getType()==r.TYPENORMAL?r.STATEOFF:this.jsxstate==null?r.STATEOFF:this.jsxstate;};d.setState=function(e){this.Vf(e,this.isOldEventProtocol(),this.getRendered());return this;};d.Vf=function(l,c,j){var Gc=false;if(this.getType()==r.TYPERADIO&&l==r.STATEON){var Lb=this.getGroupName();var t=this.getParent().findDescendants(function(k){return k instanceof r&&k.getGroupName()==Lb;},false,true,true);for(var gb=t.length-1;gb>=0;gb--){if(t[gb]!=this&&t[gb].getType()==r.TYPERADIO){t[gb].jsxstate=r.STATEOFF;var Lc=t[gb].getRendered(j);if(Lc!=null){Lc.style.backgroundImage="";Lc.childNodes[3].style.backgroundColor="";if(t[gb].getEvent(z.MENU)!=null)Lc.childNodes[2].style.backgroundImage="url("+r.IMAGEOFFMENU+")";}}else{if(t[gb]==this){if(j!=null){j.style.backgroundImage="url("+r.IMAGEON+")";j.childNodes[3].style.backgroundColor=r.BORDERCOLOR;if(this.getEvent(z.MENU)!=null)j.childNodes[2].style.backgroundImage="url("+r.IMAGEONMENU+")";}}}}Gc=true;}else{if(this.getType()==r.TYPERADIO){if(j!=null){j.style.backgroundImage="";j.childNodes[3].style.backgroundColor="";if(this.getEvent(z.MENU)!=null)j.childNodes[2].style.backgroundImage="url("+r.IMAGEOFFMENU+")";}Gc=true;}else{if(this.getType()==r.TYPECHECK){if(j!=null){if(l==r.STATEON){j.style.backgroundImage="url("+r.IMAGEON+")";j.childNodes[3].style.backgroundColor=r.BORDERCOLOR;if(this.getEvent(z.MENU)!=null)j.childNodes[2].style.backgroundImage="url("+r.IMAGEONMENU+")";}else{j.style.backgroundImage="";j.childNodes[3].style.backgroundColor="";if(this.getEvent(z.MENU)!=null)j.childNodes[2].style.backgroundImage="url("+r.IMAGEOFFMENU+")";}}Gc=true;}}}this.jsxstate=l;if(Gc&&c){var ub=null;if(c instanceof nc)ub={objEVENT:c};this.doEvent(z.CHANGE,ub);}return this;};d.setEnabled=function(n,e){if(this._jsxhotkey!=null)this._jsxhotkey.setEnabled(n==Ob.STATEENABLED);return this.jsxsupermix(n,e);};r.s5={};r.s5[nc.CLICK]="wd";r.s5[nc.KEYDOWN]=true;r.s5[nc.MOUSEDOWN]=true;r.s5[nc.MOUSEUP]=true;r.s5[nc.MOUSEOUT]=true;r.s5[nc.MOUSEOVER]=true;r.s5[nc.BLUR]="u2";r.s5[nc.FOCUS]="CL";d.k7=function(f,c,l){this.B_(f,c,l,3);};d.T5=function(){var nb=this.getRelativePosition()!=0;var Y,Cc,dc;var qc={};qc.height=22;if(nb){qc.margin=(Y=this.getMargin())!=null&&Y!=""?Y:"1 4 1 0";qc.tagname="span";qc.boxtype="relativebox";}else{qc.left=(Cc=this.getLeft())!=null&&Cc!=""?Cc:0;qc.top=(dc=this.getTop())!=null&&dc!=""?dc:0;qc.tagname="div";qc.boxtype="box";}if(this.getDivider()==1){qc.padding="0 0 0 4";qc.border="0px;0px;0px;solid 1px "+r.BORDERCOLOR;}var y=new jsx3.gui.Painted.Box(qc);qc={};qc.width=this.getImage()!=null&&this.getImage()!=""?22:3;qc.height=22;qc.tagname="span";qc.boxtype="relativebox";var Jb=new jsx3.gui.Painted.Box(qc);y.W8(Jb);qc={};if(jsx3.util.strEmpty(this.getText())){qc.width=1;}else{qc.padding="5 4 5 0";}qc.height=22;qc.tagname="span";qc.boxtype="relativebox";var xc=new jsx3.gui.Painted.Box(qc);y.W8(xc);qc={};qc.width=1;qc.height=22;qc.tagname="span";qc.boxtype="relativebox";var kb=new jsx3.gui.Painted.Box(qc);y.W8(kb);qc={};qc.width=1;qc.height=22;qc.tagname="span";qc.boxtype="relativebox";var ob=new jsx3.gui.Painted.Box(qc);y.W8(ob);return y;};d.paint=function(){this.applyDynamicProperties();var H;if((H=this.getKeyBinding())!=null){var ub=this;if(this._jsxhotkey!=null)this._jsxhotkey.destroy();this._jsxhotkey=this.doKeyBinding(function(b){ub.wd(b,ub.getRendered());},H);this._jsxhotkey.setEnabled(this.getEnabled()!=Ob.STATEDISABLED);}var Ib=this.getState()==r.STATEON?"background-image:url("+r.IMAGEON+");":"";var oc=this.T1();var pb=this.MU();var R=null,Z=null,Wb=null;if(this.getEnabled()==Ob.STATEENABLED){R=this.lM(r.s5,0);Z=this.getUriResolver().resolveURI(this.getImage());Wb="";}else{R="";Z=this.getUriResolver().resolveURI(this.getDisabledImage());Wb=jsx3.html.getCSSOpacity(0.4);}var jb=this.renderAttributes(null,true);var mc=this.RL(true);mc.setAttributes("id='"+this.getId()+"' "+"label='"+this.getName()+"' "+this.CI()+this.vH()+R+" class='jsx30toolbarbutton'"+jb);mc.setStyles(this.I6()+Ib+oc+pb+Wb+this.d9()+this.iN());var xc=mc.pQ(0);xc.setStyles("overflow:hidden;"+(Z!=null?"background-image:url("+Z+");":""));xc.setAttributes("class='jsx30toolbarbutton_img' unselectable='on'");var Hc=mc.pQ(1);Hc.setAttributes("class='jsx30toolbarbutton_lbl' unselectable='on'");var qb;if((qb=this.getText())!=null&&qb!=""){Hc.setStyles(this.QP()+this.oY()+this.g0());}else{qb="&#160;";Hc.setStyles("overflow:hidden;");}var Mc=mc.pQ(2);Mc.setAttributes("class='jsx30toolbarbutton_cap'");var Dc=mc.pQ(3);Dc.setAttributes("class='jsx30toolbarbutton_cap'");Dc.setStyles("overflow:hidden;"+(this.getState()==r.STATEON?"background-color:"+r.BORDERCOLOR+";":""));return mc.paint().join(xc.paint().join("&#160;")+Hc.paint().join(qb)+Mc.paint().join("&#160;")+Dc.paint().join("&#160;"));};d.I6=function(){return this.getEnabled()==Ob.STATEENABLED?"cursor:pointer;":"cursor:default;";};r.getVersion=function(){return "3.0.00";};d.emGetType=function(){return jsx3.gui.Matrix.EditMask.FORMAT;};d.emInit=function(s){this.jsxsupermix(s);if(this.getType()==r.TYPERADIO)this.setType(r.TYPENORMAL);this.subscribe(z.EXECUTE,this,"Mg");};d.emSetValue=function(b){};d.emGetValue=function(){return null;};d.emBeginEdit=function(s,o,b,e,f,h,m){var ec=m.childNodes[0].childNodes[0];if(ec){this.jsxsupermix(s,o,b,e,f,h,m);ec.focus();}else{return false;}};d.emPaintTemplate=function(){this.setEnabled(Ob.STATEDISABLED);var rc=this.paint();this.setEnabled(Ob.STATEENABLED);var I=this.paint();return this.PS(I,rc);};d.Mg=function(e){var U=this.emGetSession();if(U){}};});jsx3.ToolbarButton=jsx3.gui.ToolbarButton;
