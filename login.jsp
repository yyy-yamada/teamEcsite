<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<link rel="stylesheet" href="./css/style.css">
<link rel="stylesheet" href="./css/table.css">
<link rel="stylesheet" href="./css/form.css">

<title>ログイン</title>

</head>

<body>

	<s:include value="header.jsp" />

	<div id="contents">

		<h1>ログイン画面</h1>

		<s:form action="LoginAction">

			<!-- エラーメッセージがある場合は順番に表示 -->

			<s:if test="!loginIdErrorMessageList.isEmpty()">
				<div class="error">
					<div class="error-message">
						<s:iterator value="loginIdErrorMessageList">
							<s:property />
							<br>
						</s:iterator>
					</div>
				</div>

			</s:if>

			<s:if test="!passwordErrorMessageList.isEmpty()">
				<div class="error">
					<div class="error-message">
						<s:iterator value="passwordErrorMessageList">
							<s:property />
							<br>
						</s:iterator>
					</div>
				</div>
			</s:if>

			<s:if test="!loginErrorMessage.isEmpty()">
				<div class="error">
					<div class="error-message">
						<s:property value="loginErrorMessage" />
					</div>
				</div>
			</s:if>

			<table class="vertical-list-table">

				<tr>
					<th scope="row"><s:label value="ユーザーID" /></th>
					<s:if test="(#session.savedLoginId==true)&&(flg==null)">
						<td><div class="txt-box">
								<s:textfield name="loginId" class="form-txt"
									placeholder="ユーザーID" value='%{#session.lastLoginId}'
									autocomplete="off" />
							</div></td>
					</s:if>
					<s:else>
						<td><div class="txt-box">
								<s:textfield name="loginId" class="form-txt"
									value="%{loginId}" placeholder="ユーザーID" autocomplete="off" />
							</div></td>
					</s:else>

				</tr>

				<tr>
					<th scope="row"><s:label value="パスワード" /></th>
					<td><div class="txt-box">
							<s:password name="password" class="form-txt" placeholder="パスワード"
								autocomplete="off" />
						</div></td>
				</tr>

			</table>
			<br>

			<div class="checkbox-form">

				<s:if test="session.savedLoginId ==true">
					<s:checkbox name="savedLoginId" checked="checked" />
				</s:if>

				<s:else>
					<s:checkbox name="savedLoginId" />
				</s:else>

				<s:label value="ユーザーID保存" />
				<br>

			</div>

			<div class="submit_btn_box">
				<s:submit value="ログイン" class="submit_btn" onclick="goLoginAction();" />
			</div>

		</s:form>

		<div class="submit_btn_box">
			<s:form action="CreateUserAction">
				<s:submit value="新規ユーザー登録" class="submit_btn" />
			</s:form>
		</div>
		<div class="submit_btn_box">
			<s:form action="ResetPasswordAction">
				<s:submit value="パスワード再設定" class="submit_btn" />
			</s:form>
		</div>

	</div>

	<s:include value="footer.jsp" />

</body>
</html>

