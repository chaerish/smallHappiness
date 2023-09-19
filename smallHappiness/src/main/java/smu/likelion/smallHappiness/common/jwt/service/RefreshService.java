package smu.likelion.smallHappiness.common.jwt.service;

public interface RefreshService {
    String getRefresh(String refreshToken);
    void deleteRefresh(String refreshToken);
}
