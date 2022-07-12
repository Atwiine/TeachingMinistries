package com.example.tushome.Urls;

public class Urls {
    public String https = "http://172.16.0.110/shoma/";


    public String LOGIN_URL = https + "login.php";///
    public String CHECKLOGIN_URL = https + "check_login.php";///
    public String FIRSTREG_URL = https + "register.php";///
    public String READERBOOKS_URL = https + "r_books.php";///
    public String SEND_FEEDBACK = https + "send_feedback.php";///
    public String SEND_ONLINE_EMAIL_ORDER = https + "send_online_order.php";///
    public String SEND_HARD_COPY_ORDER = https + "send_hard_order.php";///


    public String UPLOAD_BOOK_COVER = https + "upload_book.php"; ///
    public String AUTHOR_BOOKS = https + "author_books.php"; ////
    public String OTHERS_BOOKS = https + "others_books.php";//
    public String FEEDBACK = https + "r_feedback.php";///
    public String COUNTFEEDBACK = https + "count_feedback.php";///
    public String DELETE_BOOK_URL = https + "delete_book.php";///
    public String UPDATE_BOOK_COVER = https + "update_book.php"; ////
    public String REQUESTS_BOOKS = https + "order_request.php";///




    public String USERDETS_URL = https + "read_user.php";///
    public String EDITACCOUNT = https + "edit_profile.php";// half

    public String HELPMESSAGE_URL = https + "help_message.php"; // half

    public String ALLREQUESTS_URL = https + "all_requests.php"; //

    public String CONFIRM_REQUEST = https + "confirm_requests.php";
    public String DENY_REQUEST = https + "deny_requests.php";
    public String READER_NOTIFICATION = https + "reader_notifications.php";


    /*private chats*/
    public String MESSAGES_LIST = https + "message_list.php";///
    public String SEND_MESSAGE = https + "send_message.php";///

    public String GET_CHAT_USERS = https + "read_message_users.php";///use status to see if they have un read shit
    public String SEND_SINGLE_CHAT = https + "send_private_message.php";
    public String GET_POST_USERS = https + "read_post_users.php";
    public String GET_SINGLE_MESSAGES = https + "message_private_list.php";

    /*posts*/
    public String SEND_SINGLE_POST = https + "send_private_post.php";
    public String SEND_GROUP_POST = https + "send_group_post.php";
    public String GET_GROUP_POST = https + "message_posts.php";
    public String GET_SINGLE_POST = https + "message_private_posts.php";
    //later
    public String SEND_COMMENT_POST = https + "send_post_comments.php";
    public String GET_COMMENT_POST = https + "get_post_comments.php";
    public String UPDATE_LIKES_POST = https + "update_likes.php";
    public String UPDATE_DISLIKES_POST = https + "update_dislikes.php";

    //total new messages and posts
    public String GET_NO_NEW_MESSAGES = https + "get_no_new_messages.php";//
    public String GET_NO_NEW_POSTS = https + "get_no_new_posts.php";//
    public String GET_NO_COMMENTS = https + "get_no_comments.php";//
    public String GET_LAST_MESSAGES = https + "get_last_messages.php";//


    /*updating seen messages and posts*/
    public String USEENPRIATE_POST = https + "useen_private_posts.php";//
    public String USEENPRIATE_CHAT = https + "useen_private_chat.php";//

}
