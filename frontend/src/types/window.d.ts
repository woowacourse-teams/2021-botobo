interface Window {
  __INITIAL_STATE__?: {
    userState?: import('recoil').RecoilState<
      import('.').UserInfoResponse | null
    >;
    workbookState?: import('recoil').RecoilState<
      import('../recoil/workbookState').WorkbookState
    >;
    rankingWorkbooksState?: import('recoil').RecoilState<
      import('.').RankingWorkbookResponse[]
    >;
    rankingSearchKeywordsState?: import('recoil').RecoilState<
      import('.').RankingSearchKeywordResponse[]
    >;
  };
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  Kakao: any;
}
