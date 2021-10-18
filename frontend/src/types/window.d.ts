interface Window {
  __INITIAL_STATE__?: {
    userState?: import('recoil').RecoilState<
      import('.').UserInfoResponse | null
    >;
    workbookState?: import('recoil').RecoilState<
      import('../recoil/workbookState').WorkbookState
    >;
  };
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  Kakao: any;
}
