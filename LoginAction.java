package com.internousdev.gerbera.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.gerbera.dao.CartInfoDAO;
import com.internousdev.gerbera.dao.UserInfoDAO;
import com.internousdev.gerbera.dto.CartInfoDTO;
import com.internousdev.gerbera.dto.UserInfoDTO;
import com.internousdev.gerbera.util.InputChecker;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport implements SessionAware {

	private String loginId;
	private String password;
	private boolean savedLoginId;
	private String flg="1";

	private List<String> loginIdErrorMessageList =new ArrayList<String>();
	private List<String> passwordErrorMessageList =new ArrayList<String>();
	private String loginErrorMessage = "";

	private Map<String,Object> session;

	public String execute(){

		if(!session.containsKey("mCategoryList")) {
			return "sessionTimeOut";
		}

		if(session.containsKey("createUserCompleteFlg")){
			loginId = session.get("createLoginId").toString();
			password = session.get("password").toString();
			session.remove("createLoginId");
			session.remove("password");
			session.remove("createUserCompleteFlg");
		}

		String result =ERROR;

		if(savedLoginId==false){
				session.put("savedLoginId" ,false);
				session.remove("lastLoginId");
		}

		//入力チェック（未入力,桁数,文字種）
		InputChecker inputChecker =new InputChecker();

		loginIdErrorMessageList = inputChecker.doCheck("ユーザーID", loginId, 1, 8, true, false, false, true, false, false, false, false, false);
		passwordErrorMessageList = inputChecker.doCheck("パスワード", password, 1, 16, true, false, false, true, false, false, false, false, false);

		//入力チェックでエラーが出なかった場合
		if(loginIdErrorMessageList.size()==0
		&& passwordErrorMessageList.size()==0){

			UserInfoDAO userInfoDao =new UserInfoDAO();

		    //DBの会員情報テーブルにユーザーIDとパスワードが一致するユーザーが存在しているか確認
			if(userInfoDao.isExistUserInfo (loginId, password)){
				if(userInfoDao.login(loginId, password)>0){

					UserInfoDTO userInfoDTO =userInfoDao.getUserInfo(loginId, password);
					session.put("loginId", userInfoDTO.getUserId());

					int cartFlg =0;
					CartInfoDAO cartInfoDao =new CartInfoDAO();
					cartFlg =cartInfoDao.linkToLoginId(String.valueOf(session.get("tempUserId")), loginId);//カートに商品が追加されており、そのカート情報テーブル内の"tempUserId"がnull,"user_id"がloginIdに更新された件数

					//カートフラグを保持している場合（未ログイン時に商品をカートに追加していた場合）
					if(cartFlg>0){

						//未ログイン時とログイン後のカート情報を統合
						int integrationCartInfo =cartInfoDao.integrationCartInfo(loginId);

						if(integrationCartInfo<=0){
							loginErrorMessage ="カート情報の取得に失敗しました。";
							return ERROR;
						}

						//更新後のカート情報テーブルのデータを反映させる
						List<CartInfoDTO> cartInfoDtoList =cartInfoDao.getCartInfo(loginId);
						session.put("cartInfoDtoList",cartInfoDtoList);

						int totalPrice =cartInfoDao.getTotalPrice(loginId);
						session.put("totalPrice",totalPrice);

						result="cart";

					}else{
					//カートフラグを保持していない場合
						result=SUCCESS;
						}
					}

				//ユーザーID保存にチェックありの場合
				if(savedLoginId ==true){
					session.put("savedLoginId", true);
					session.put("loginId", loginId);

					}else{
						session.put("savedLoginId" ,false);
						session.remove("lastLoginId");
						}

				session.put("logined" ,1);//ログイン済の状態

			}else{
				loginErrorMessage ="入力されたユーザーIDまたはパスワードが異なります。";
				result=ERROR;
				}
		}else{
			session.put("logined" ,0);//未ログイン状態

		}
		return result;
	}

	public String getFlg(){
		return flg;
	}

	public String getLoginId(){
		return loginId;
	}
	public void setLoginId(String loginId){
		this.loginId =loginId;
	}

	public String getPassword(){
		return password;
	}
	public void setPassword (String password){
		this.password =password;
	}

	public boolean isSavedLoginId(){
		return savedLoginId;
	}
	public void setSavedLoginId(boolean savedLoginId){
		this.savedLoginId =savedLoginId;
	}

	public List<String> getLoginIdErrorMessageList(){
		return loginIdErrorMessageList;
	}
	public void setLoginIdErrorMessageList(List<String> loginIdErrorMessageList){
		this.loginIdErrorMessageList =loginIdErrorMessageList;
	}

	public List<String> getPasswordErrorMessageList(){
		return passwordErrorMessageList;
	}
	public void setPasswordErrorMessageList(List<String> passwordErrorMessageList){
		this.passwordErrorMessageList =passwordErrorMessageList;
	}

	public String getLoginErrorMessage() {
		return loginErrorMessage;
	}

	public Map<String, Object> getSession(){
		return session;
	}
	public void setSession(Map<String, Object> session){
		this.session =session;
	}

}
