import { waitFor } from '@testing-library/react';
import React from 'react';

import { MainPage } from '../pages';
import { render } from '../test-utils';

describe('메인 페이지 테스트', () => {
  test('렌더링 테스트', async () => {
    const { getByText } = render(<MainPage />);

    await waitFor(() => {
      expect(getByText('Java')).toBeVisible();
    });
  });
});
