package com.imenu.fr.restaurant.api.service;

import com.imenu.fr.restaurant.api.model.Status;
import com.imenu.fr.restaurant.api.model.devicetoken.UpdteTokenRequest;
import com.imenu.fr.restaurant.api.model.login.LoginRequest;
import com.imenu.fr.restaurant.api.model.login.LoginResponse;
import com.imenu.fr.restaurant.api.model.order.OrderResponse;
import com.imenu.fr.restaurant.api.model.orderoperation.OrderOperationRequest;
import com.imenu.fr.restaurant.api.model.updateitemstatus.UpdateItemStatusRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Bhupinder on 29/3/17.
 */
public interface Service {

    //    @POST("signup")
//    Call<NewUserResponse> registerUser(@Body NewUserRequest newUserRequest);
//
//    @PUT("otp_verification")
//    Call<VerifyOtpResponse> verifyOtpAfterRegistration(@Header("token") String token, @Body OtpVerificationRequest otpVerificationRequest);
//
    @POST("owner")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("updateToken")
    Call<Status> updateToken(@Body UpdteTokenRequest updteTokenRequest);

    @POST("orderApproval")
    Call<Status> orderOperation(@Body OrderOperationRequest orderOperationRequest);


    @POST("updateStatus")
    Call<Status> updateItemStatus(@Body UpdateItemStatusRequest updateItemStatusRequest);


    //
//    @POST("is_fb_exists")
//    Call<LoginResponse> checkFacebookUserExistsOrNot(@Body FbUserExistsRequest fbUserExistsRequest);
//
//    @PUT("resend_otp")
//    Call<VerifyOtpResponse> resendRegisterOtp(@Header("token") String token, @Body NewUserResponse newUserResponse);
//
    @GET("pendingOrder")
    Call<OrderResponse> getOrder(@Query("store_id") int store_id,
                                 @Query("status") int status,
                                 @Query("limit") int limit,
                                 @Query("offset") int offset);

    @GET("orderHistory")
    Call<OrderResponse> getOrderDetails(@Query("order_id") int order_id);
//
//    @PUT("customer_address/default")
//    Call<Status> setDefaultAddress(@Header("token") String token, @Body SetDefault setDefault);
//
//    @PUT("customer_booking_action")
//    Call<Status> cancelBooking(@Header("token") String token, @Body CancelBooking cancelBooking);
//
//    @HTTP(method = "DELETE", path = "customer_address", hasBody = true)
//    Call<Status> deleteAddress(@Header("token") String token, @Body SetDefault setDefault);
//
//    @DELETE("delete_account")
//    Call<Status> deleteAccount(@Header("token") String token);
//
//    @HTTP(method = "DELETE", path = "stylist_under_salon", hasBody = true)
//    Call<Status> deleteStylist(@Header("token") String token, @Body Stylist stylist);
//
//    @POST("forgot_password/otp_code")
//    Call<VerifyOtpResponse> generateForgotPassOtp(@Body ForgotPasswordRequest forgotPasswordRequest);
//
//
//    @POST("forgot_password/otp_verify")
//    Call<VerifyOtpResponse> verifyForgotOtp(@Body VerifyForgotPassRequest verifyForgotPassRequest);
//
//    @POST("reset_password")
//    Call<ApiSuccessResponse> resetForgotPass(@Body ResetPasswordRequest resetPasswordRequest);
//
//    @PUT("change_password")
//    Call<Status> changePassword(@Header("token") String token, @Body ChangePassword changePassword);
//
//    @POST("profile_pic")
//    Call<Status> uploadProfilePicture(@Header("token") String token, @Body ProfilePic profilePic);
//
//    @PUT("push_notification")
//    Call<PushNotificationResponse> pushNotification(@Header("token") String token, @Body PushNotifications pushNotifications);
//
//    @GET("customer_address")
//    Call<AddressResponse> getSavedAddresses(@Header("token") String token);
//
//    @GET("services")
//    Call<GetServiceResponse> getServices();
//
//    @GET("stylist_under_salon")
//    Call<StylistsListResponse> getStylistList(@Header("token") String token);
//
//
//    @GET("location")
//    Call<LocationResponse> getLocations(@Header("token") String token);
//
//    @POST("location")
//    Call<LocationResponse> getSubLocations(@Header("token") String token, @Body Location location);
//
//    @PUT("edit_profile")
//    Call<EditProfileResponse> updateProfile(@Header("token") String token, @Body EditProfileRequest editProfileRequest);
//
//    @POST("customer_address")
//    Call<Status> AddAddress(@Header("token") String token, @Body Address address);
//
//    @POST("contact_us")
//    Call<Status> sendMessage(@Header("token") String token, @Body ContactUs contactUs);
//
//    @POST("instant_lookup")
//    Call<LookUpResponse> findAvailable(@Header("token") String token, @Body BookNow bookNow);
//
//    @POST("instant_lookup_response")
//    Call<LookUpResponse> isBookingAccepted(@Header("token") String token, @Body BookingID bookingID);
//
//    @POST("stylist_book_later   ")
//    Call<StylistResponse> bookLater(@Header("token") String token, @Body BookStylist bookStylist);
//
//    @PUT("customer_address")
//    Call<Status> updateAddress(@Header("token") String token, @Body Address address);
//
//    @PUT("otp_verification")
//    Call<VerifyOtpResponse> verifyOtpOnEditProfile(@Header("token") String token, @Body EditProfileOtpVerificationRequest editProfileOtpVerificationRequest);
//
//    @PUT("resend_otp")
//    Call<VerifyOtpResponse> resendChangePhoneOtp(@Header("token") String token, @Body EditProfileResendOtp editProfileResendOtp);
//
//    @GET("faqs")
//    Call<FaqResponse> getFaqs(@Header("token") String token);
//
//    @GET("total_loyalty_points")
//    Call<LoyaltyResponse> getLoyaltyPoints(@Header("token") String token);
//
//    @GET("promo_codes")
//    Call<PromoCodeResponse> getPromoCodes(@Header("token") String token);
//
//    @GET("profile_approved")
//    Call<Approved> isProfileApproved(@Header("token") String token);
//
//    @POST("stylist_details")
//    Call<StylistDetailResponse> getStylistDetails(@Header("token") String token, @Body StylistID stylistID);
//
//    @POST("favourite_stylist")
//    Call<ApiSuccessResponse> addToFavorite(@Header("token") String token, @Body FavouriteRequest favouriteRequest);
//
//    @GET("favourite_stylist")
//    Call<StylistResponse> getFavoriteList(@Header("token") String token);
//
//    @GET("favourite_stylist")
//    Call<StylistResponse> getStylistsForSalonOwner(@Header("token") String token);
//
//    @PUT("onboard_details")
//    Call<Status> onBoard(@Header("token") String token, @Body OnBoardingSalonOwner onBoardingSalonOwner);
//
//    @GET("salon_owner_dashboard")
//    Call<SalonOwnerDashboardResponse> getSalonOwnderDashboardData(@Header("token") String token, @Query("date") String date, @Query("type") String type);
//
//    @GET("freelancer_stylist_dashboard")
//    Call<SalonFreelancerDashboardResponse> getSalonFreelancerDashboardData(@Header("token") String token, @Query("date") String date, @Query("type") String type);
//
//
//    @GET("address_titles")
//    Call<LocationResponse> getAddressTitles(@Header("token") String token);
//
//    @GET("stylist_under_salon")
//    Call<StylistResponse> getStylists(@Header("token") String token);
//
//
//    @GET("salon_stylist_dashboard")
//    Call<StylishUnderSalonDashboardResponse> getUnderSalonStylishDashboardData(@Header("token") String token, @Query("date") String date);
//
//    @PUT("stylist_booking_action")
//    Call<FreelancerBookingActionResponse> acceptBooking(@Header("token") String token, @Body FreelancerBookingActionRequest freelancerBookingAcceptRequest);
//
//    @POST("stylist_bookings")
//    Call<FreelancerMyBookingResponse> getFreelancerBookings(@Header("token") String token, @Body FreelancerMyBookingRequest freelancerMyBookingRequest);
//
//    @POST("salon_owner_bookings")
//    Call<OwnerMyBookingResponse> getOwnerBookings(@Header("token") String token, @Body OwnerMyBookingRequest ownerMyBookingRequest);
//
//
//    @GET("payment_bookings_total_amount")
//    Call<TotalPaymentResponse> getTotalPayment(@Header("token") String token);
//
//    @POST("availability")
//    Call<AvailabilityResponse> getAvailability(@Header("token") String token, @Body Availability availability);
//
//    @PUT("availability")
//    Call<AvailabilityResponse> setAvailability(@Header("token") String token, @Body SetAvailability setAvailability);
//
//    @HTTP(method = "DELETE", path = "availability", hasBody = true)
//    Call<Status> deleteTimeSlot(@Header("token") String token, @Body DeleteSlot deleteSlot);
//
//    @GET("portfolio_images")
//    Call<ManagePortfolioAllImageResponse> getPortfolioImages(@Header("token") String token, @Query("stylist_id") String date);
//
//    @POST("portfolio_images")
//    Call<ManagePortfolioResponse> addPorfolioImage(@Header("token") String token, @Body AddImagePorfolioRequest managePorfolioRequest);
//
//    @DELETE("portfolio_images")
//    Call<ManagePortfolioResponse> deletePortfolioImage(@Header("token") String token, @Query("stylist_id") String stylistId, @Query("_id") String id);
//
//    @PUT("manage_payment_info")
//    Call<Status> addPaymentInfo(@Header("token") String token, @Body ManagePaymentInfoRequest managePaymentInfoRequest);
//
//    @GET("manage_payment_info")
//    Call<ManagePaymentInfoResponse> getPaymentInfo(@Header("token") String token);
//
//    @POST("stylist_under_salon")
//    Call<AddStylistResponse> addStylist(@Header("token") String token, @Body AddStylistRequest addStylistRequest);
//
//    @PUT("stylist_under_salon")
//    Call<AddStylistResponse> updateStylist(@Header("token") String token, @Body AddStylistRequest addStylistRequest);
//
//    @PUT("edit_profile")
//    Call<FreelancerOnBoardResponse> editProfile(@Header("token") String token, @Body FreelancerOnBoardRequest freelancerOnBoardRequest);
//
//    @GET("edit_profile")
//    Call<EditProfileResponse> getProfile(@Header("token") String token);
//
//    @PUT("onboard_details")
//    Call<FreelancerOnBoardResponse> onBoardFreelancer(@Header("token") String token, @Body FreelancerOnBoardRequest freelancerOnBoardRequest);
//
//
//    @PUT("current_coordinates")
//    Call<Status> updateLatLng(@Header("token") String token, @Body UpdateLatLngRequest updateLatLngRequest);
//
//    @PUT("manage_location")
//    Call<Status> updateCurrentLocationToggleStatus(@Header("token") String token, @Body UpdateToggleStatusRequest updateToggleStatusRequest);

}


