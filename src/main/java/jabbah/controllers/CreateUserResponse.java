	package jabbah.controllers;

	public class CreateUserResponse {
		String response;
		int httpCode;
		
		public CreateUserResponse (String s, int code) {
			this.response = s;
			this.httpCode = code;
		}
		
		// 200 means success
		public CreateUserResponse (String s) {
			this.response = s;
			this.httpCode = 200;
		}
		
		public String toString() {
			return "Response(" + response + ")";
		}
	}
