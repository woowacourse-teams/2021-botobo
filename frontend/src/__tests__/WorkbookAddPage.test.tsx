import React from 'react';
import { RecoilRoot } from 'recoil';

import { PageHeader } from '../components';
import { ROUTE } from '../constants';
import { WorkbookAddPage } from '../pages';
import { loginState } from '../recoil';
import { render, screen } from '../test-utils';

describe('문제집 추가 페이지 테스트', () => {
  beforeAll(() => {
    render(
      <RecoilRoot initializeState={(snap) => snap.set(loginState, true)}>
        <PageHeader title={ROUTE.WORKBOOK_ADD.TITLE} />
        <WorkbookAddPage />
      </RecoilRoot>
    );
  });

  test('렌더링 테스트', async () => {
    expect(screen.getByText('문제집 추가')).toBeVisible();
  });

  //TODO: 폼 입력 후 확인을 눌렀을 때, 추가 성공 테스트
});
