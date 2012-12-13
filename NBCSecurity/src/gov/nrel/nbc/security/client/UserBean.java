package gov.nrel.nbc.security.client;


public class UserBean {
		private String j_username = null;
		private String j_password = null;
		private String j_password2 = null;
		private String j_first = null;
		private String j_last = null;
		private String j_email = null;
		private String j_hidden = null;
		private String j_message = null;
		/**
		 * @return the j_username
		 */
		public String getJ_username() {
			return j_username;
		}
		/**
		 * @param j_username the j_username to set
		 */
		public void setJ_username(String j_username) {
			this.j_username = j_username;
			StaticUserBean.setUsername(j_username);
		}
		/**
		 * @return the j_password
		 */
		public String getJ_password() {
			return j_password;
		}
		/**
		 * @param j_password the j_password to set
		 */
		public void setJ_password(String j_password) {
			this.j_password = j_password;
			StaticUserBean.setPassword(j_password);
		}
		/**
		 * @return the j_password2
		 */
		public String getJ_password2() {
			return j_password2;
		}
		/**
		 * @param j_password2 the j_password2 to set
		 */
		public void setJ_password2(String j_password2) {
			this.j_password2 = j_password2;
			StaticUserBean.setPassword2(j_password2);
		}
		/**
		 * @return the j_first
		 */
		public String getJ_first() {
			return j_first;
		}
		/**
		 * @param j_first the j_first to set
		 */
		public void setJ_first(String j_first) {
			this.j_first = j_first;
			StaticUserBean.setFirst(j_first);
		}
		/**
		 * @return the j_last
		 */
		public String getJ_last() {
			return j_last;
		}
		/**
		 * @param j_last the j_last to set
		 */
		public void setJ_last(String j_last) {
			this.j_last = j_last;
			StaticUserBean.setLast(j_last);
		}
		/**
		 * @return the j_email
		 */
		public String getJ_email() {
			return j_email;
		}
		/**
		 * @param j_email the j_email to set
		 */
		public void setJ_email(String j_email) {
			this.j_email = j_email;
			StaticUserBean.setEmail(j_email);
		}
		/**
		 * @return the j_hidden
		 */
		public String getJ_hidden() {
			return j_hidden;
		}
		/**
		 * @param j_hidden the j_hidden to set
		 */
		public void setJ_hidden(String j_hidden) {
			this.j_hidden = j_hidden;
			StaticUserBean.setHidden(j_hidden);
		}
		/**
		 * @param j_message the j_message to set
		 */
		public void setJ_message(String j_message) {
			this.j_message = j_message;
		}
		/**
		 * @return the j_message
		 */
		public String getJ_message() {
			return StaticUserBean.getMessage();
		}
}

