interface Window {
  __INITIAL_STATE__?: {
    userState?: import('recoil').RecoilState<
      import('.').UserInfoResponse | null
    >;
    workbookState?: import('recoil').RecoilState<
      import('../recoil/workbookState').WorkbookState
    >;
  };
}
