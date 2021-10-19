interface Window {
  __INITIAL_STATE__?: {
    userState?: import('recoil').RecoilState<
      import('.').UserInfoResponse | null
    >;
    workbookState?: import('recoil').RecoilState<
      import('../recoil/workbookState').WorkbookState
    >;
    workbookRankingState?: import('recoil').RecoilState<
      import('.').WorkbookRankingResponse[]
    >;
    searchKeywordRankingState?: import('recoil').RecoilState<
      import('.').SearchKeywordRankingResponse[]
    >;
  };
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  Kakao: any;
}
