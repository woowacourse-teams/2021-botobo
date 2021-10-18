import { SetRecoilState } from 'recoil';

import { Entries } from '../types/utils';
import { userState } from './userState';
import { workbookState } from './workbookState';

export { publicSearchResultState } from './searchState';
export {
  quizState,
  quizModeState,
  hasQuizTimeState,
  quizTimeState,
} from './quizState';
export { userState } from './userState';
export {
  shouldWorkbookUpdateState,
  workbookState,
  publicWorkbookState,
} from './workbookState';
export { rankingWorkbooksState, rankingSearchKeywordsState } from './rankState';

export interface InitialState {
  userState?: typeof userState;
  workbookState?: typeof workbookState;
}

const entries = <T>(obj: T) => Object.entries(obj) as Entries<T>;

const ssrState = {
  userState,
  workbookState,
} as const;

export const initRecoilStateWithSsr = (
  set: SetRecoilState,
  initialState?: InitialState
) => {
  if (!initialState) return;

  entries(initialState).forEach((state) => {
    if (!state) return;

    const [key, value] = state;

    // TODO: userState를 선택했을 때 userState의 type이 할당될 수 있도록 구현하기. 아직은 못해서 any로 두었음
    const recoilValue = ssrState[key] as any;

    set(recoilValue, value);
  });
};
