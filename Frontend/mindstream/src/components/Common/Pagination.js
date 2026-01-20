import React from 'react';
import { ChevronLeft, ChevronRight, MoreHoriz } from '@mui/icons-material';
import './Common.css';

const Pagination = ({
  currentPage,
  totalPages,
  onPageChange,
  showInfo = false,
  className = ''
}) => {
  const maxVisiblePages = 5;

  const getPageNumbers = () => {
    const pages = [];
    let startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
    let endPage = startPage + maxVisiblePages - 1;

    if (endPage > totalPages) {
      endPage = totalPages;
      startPage = Math.max(1, endPage - maxVisiblePages + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }

    return pages;
  };

  const handlePageChange = (page) => {
    if (page >= 1 && page <= totalPages && page !== currentPage) {
      onPageChange(page);
    }
  };

  const pageNumbers = getPageNumbers();

  if (totalPages <= 1) {
    return null;
  }

  return (
    <div className={`pagination ${className}`}>
      <button
        className="pagination-button"
        onClick={() => handlePageChange(currentPage - 1)}
        disabled={currentPage === 1}
      >
        <ChevronLeft className="pagination-button-icon" />
      </button>

      {pageNumbers[0] > 1 && (
        <>
          <button
            className="pagination-button"
            onClick={() => handlePageChange(1)}
          >
            1
          </button>
          {pageNumbers[0] > 2 && (
            <span className="pagination-ellipsis">
              <MoreHoriz />
            </span>
          )}
        </>
      )}

      {pageNumbers.map((page) => (
        <button
          key={page}
          className={`pagination-button ${currentPage === page ? 'active' : ''}`}
          onClick={() => handlePageChange(page)}
        >
          {page}
        </button>
      ))}

      {pageNumbers[pageNumbers.length - 1] < totalPages && (
        <>
          {pageNumbers[pageNumbers.length - 1] < totalPages - 1 && (
            <span className="pagination-ellipsis">
              <MoreHoriz />
            </span>
          )}
          <button
            className="pagination-button"
            onClick={() => handlePageChange(totalPages)}
          >
            {totalPages}
          </button>
        </>
      )}

      <button
        className="pagination-button"
        onClick={() => handlePageChange(currentPage + 1)}
        disabled={currentPage === totalPages}
      >
        <ChevronRight className="pagination-button-icon" />
      </button>

      {showInfo && (
        <div className="pagination-info">
          Page {currentPage} of {totalPages}
        </div>
      )}
    </div>
  );
};

export default Pagination;