interface Window {
  __INITIAL_STATE__?: {
    userInfo: import('.').UserInfoResponse;
    workbookInfo: import('../recoil/workbookState').WorkbookState;
  };
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  Kakao: any;
}
