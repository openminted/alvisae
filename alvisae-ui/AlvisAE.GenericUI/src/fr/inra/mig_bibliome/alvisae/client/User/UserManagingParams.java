/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.User;

import fr.inra.mig_bibliome.alvisae.client.Config.History.PlaceParams;

/**
 * @author fpapazian
 */
public class UserManagingParams {

    public static UserManagingParams createFromToken(String token) {
        return new UserManagingParams(token);
    }
    private Integer userId = null;

    public UserManagingParams(Integer userId) {
        this.userId = userId;
    }

    public UserManagingParams(String token) {
        parseToken(token);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String createToken() {
        return "u" + PlaceParams.ParamsProcessor.KEYVAL_SEP + getUserId() + PlaceParams.ParamsProcessor.PARAM_SEP ;
    }

    public class UserManagingParamsProcessor implements PlaceParams.ParamsProcessor {

        @Override
        public void processParam(String key, String value) {
            if (key.equals("u") && userId == null) {
                try {
                    userId = Integer.valueOf(value);
                    setUserId(userId);
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    protected void parseToken(String token) {
        PlaceParams.parseToken(token, new UserManagingParamsProcessor());
    }
}
