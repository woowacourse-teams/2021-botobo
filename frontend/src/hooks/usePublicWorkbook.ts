import { useEffect, useState } from 'react';
import { useRecoilState, useSetRecoilState } from 'recoil';

import { getPublicWorkbookAsync } from '../api';
import { publicWorkbookIdState, searchKeywordState } from '../recoil';
import { PublicWorkbookResponse } from '../types';

// TODO: 추상화 잘 해보자!(useSearch 느낌)
const usePublicWorkbook = () => {
  const [keyword, setKeyword] = useRecoilState(searchKeywordState);
  const [inputValue, setInputValue] = useState(keyword);
  const [publicWorkbooks, setPublicWorkbooks] = useState<
    PublicWorkbookResponse[]
  >([]);
  const setPublicWorkbookId = useSetRecoilState(publicWorkbookIdState);

  const search = async (value: string) => {
    try {
      const data = await getPublicWorkbookAsync(value);
      setPublicWorkbooks(data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    const resetKeyword = () => setKeyword('');

    search(inputValue);
    window.addEventListener('popstate', resetKeyword);

    return () => window.removeEventListener('popstate', resetKeyword);
  }, []);

  return {
    keyword,
    setKeyword,
    inputValue,
    setInputValue,
    publicWorkbooks,
    setPublicWorkbooks,
    setPublicWorkbookId,
    search,
  };
};

export default usePublicWorkbook;
